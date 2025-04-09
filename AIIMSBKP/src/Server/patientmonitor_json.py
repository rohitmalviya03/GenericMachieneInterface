import socket 
import time
import logging
import threading
from datetime import datetime
import uuid
import hl7
import json
import requests

# MLLP constants
START_BLOCK = b"\x0B"  # <VT>
END_BLOCK = b"\x1C"    # <FS>
CARRIAGE_RETURN = b"\x0D"  # <CR>

# Configure logging
logging.basicConfig(
    filename="patientmonitor.log",
    level=logging.DEBUG,  # Changed to DEBUG for more detailed logs
    format="%(asctime)s - %(levelname)s - %(message)s",
)

def mllp_encode(message):
    """Encapsulate the HL7 message using MLLP."""
    return START_BLOCK + message.encode("latin-1") + END_BLOCK + CARRIAGE_RETURN

def generate_timestamp():
    """Generate a current timestamp in the format YYYYMMDDHHMMSS."""
    return datetime.now().strftime("%Y%m%d%H%M%S")

def generate_query_id():
    """Generate a unique query ID that is less than 16 bytes."""
    return str(uuid.uuid4().int)[:15]  # Generate a numeric ID truncated to 15 characters


def parse_hl7_to_json(hl7_message):
    """Parses an HL7 message string and converts it to the desired JSON format for SpO2 and PR."""
    try:
        parsed_message = hl7.parse(hl7_message)
        json_data = []
        packet_timestamp = datetime.now().strftime("%Y%m%d%H%M%S") + "-0500"
        machine_id = "1001"  # Example machine ID
        patient_id = "989262400001438"  # Example patient ID

        for segment in parsed_message:
            if segment[0][0] == "OBX":
                # Extract and split parameter ID and name
                param_field = segment[3][0] if len(segment[3]) > 0 else ""
                param_field = str(param_field)
                param_id, param_name = (param_field.split("^", 1) if "^" in param_field else (param_field, ""))

                # Process only "SpO2" and "PR"
                if param_name not in ["SpO2", "PR", "HR"]:
                    continue

                param_value = segment[5][0] if len(segment[5]) > 0 else ""
                param_unit = segment[6][0] if len(segment[6]) > 0 else ""
                param_reference_range = segment[7][0] if len(segment[7]) > 0 else ""
                timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")

                json_data.append({
                    "patient_id": patient_id,
                    "param_id": param_id,
                    "param_name": param_name,
                    "param_value": param_value,
                    "param_unit": param_unit,
                    "param_referenceRange": param_reference_range,
                    "timestamp": timestamp,
                    "packettimestamp": packet_timestamp,
                    "machineId": machine_id
                })

        # Return JSON for SpO2, PR and HR only
        return json.dumps(json_data, indent=4) if json_data else None

    except Exception as e:
        logging.error(f"Error parsing HL7 message to JSON: {e}")
        return None

        
def connect_to_monitor(host, port):
    """Establish a connection to the patient monitor."""
    try:
        client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        client_socket.connect((host, port))
        logging.info(f"Connection established with {host}:{port}")
        return client_socket
    except Exception as e:
        logging.error(f"Connection error: {e}")
        return None

def send_query_message(client_socket, qry_message):
    """Send the entire query message post MLLP encoding."""
    try:
        encoded_message = mllp_encode(qry_message)
        logging.debug(f"Encoded QRY message: {encoded_message}")
        client_socket.sendall(encoded_message)
        print("Encoded Message Sent:", encoded_message.decode('latin-1'))
        logging.debug("Encoded Message Sent:", encoded_message.decode('latin-1'))
        logging.info(f"Sent QRY message.{encoded_message}")
    except Exception as e:
        logging.error(f"Error sending QRY message: {e}")
        client_socket.close()

def send_tcp_echo(client_socket, running):
    """Send a TCP echo message every second to maintain the connection."""
    echo_message = f"MSH|\\&|||||||ORU^R01|106|P|2.3.1|"
    try:
        while running.is_set():
            encoded_echo_message = mllp_encode(echo_message)
            client_socket.sendall(encoded_echo_message)
            logging.info("Sent TCP Echo message.")
            logging.debug(f"Encoded TCP Echo message: {encoded_echo_message}")
            print(f"Encoded TCP Echo message: {encoded_echo_message}")
            time.sleep(1)
    except Exception as e:
        logging.error(f"Error sending echo message: {e}")
        client_socket.close()


def receive_data(client_socket, running):
    """Receive and process real-time data from the patient monitor."""
    buffer = b""  # Buffer to store partial messages
    try:
        while running.is_set():
            chunk = client_socket.recv(4096)
            if not chunk:
                logging.warning("Connection closed by server.")
                break

            buffer += chunk

            # Process complete messages in the buffer
            while START_BLOCK in buffer and END_BLOCK + CARRIAGE_RETURN in buffer:
                start = buffer.find(START_BLOCK)
                end = buffer.find(END_BLOCK + CARRIAGE_RETURN)

                if start != -1 and end != -1 and end > start:
                    # Extract a complete message
                    complete_message = buffer[start + 1: end]
                    buffer = buffer[end + 2:]

                    try:
                        # Decode and process the complete message
                        received_message = complete_message.decode("latin-1", errors="replace")
                        logging.info(f"Received data: {received_message}")
                        print(f"Received data: {received_message}")

                        # Convert to JSON and print
                        json_output = parse_hl7_to_json(received_message)
                        if json_output:
                            print(f"Converted to JSON:\n{json_output}")
                            logging.info(f"Converted to JSON:\n{json_output}")

                            # Parse JSON to ensure it's a list
                            if isinstance(json_output, str):
                                json_output = json.loads(json_output)

                            # Ensure payload is a list
                            if not isinstance(json_output, list):
                                json_output = [json_output]

                            # Base URL for the API
                            aiims_url = "https://aiimsjodhpur.uat.dcservices.in"  # Replace with your correct base URL

                            # Complete API URL
                            url = f"{aiims_url}/OT_INTEGRATION/service/api/saveresult/"

                            # Headers for the request
                            headers = {
                                "Content-Type": "application/json",
                            }

                            # Send the POST request with the JSON payload
                            response = requests.post(url, headers=headers, json=json_output)

                            # Print the response status code and body
                            print(f"Response status code: {response.status_code}")
                            logging.info(f"Response status code: {response.status_code}")
                            print(f"Response body: {response.text}")
                            logging.info(f"Response body: {response.text}")

                    except Exception as e:
                        logging.error(f"Error decoding message: {e}")
                        print(f"Error decoding message: {e}")
                else:
                    break
    except Exception as e:
        logging.error(f"Error receiving data: {e}")


# Other functions remain unchanged

def main():
    # IP and port of the patient monitor
   # host = "192.168.0.103"  # Replace with the actual IP address
    #port = 4601  # Port for real-time interface

    if len(sys.argv) < 5:
        print("Usage: python script.py <path> <server_ip> <server_port> <cr_no>")
        sys.exit(1)

    # Fetch the arguments
    script_name = sys.argv[0]  # The script name itself
    path = sys.argv[1]         # First argument (path)
    server_ip = sys.argv[2]    # Second argument (server_ip)
    server_port = sys.argv[3]  # Third argument (server_port)
    cr_no = sys.argv[4]        # Fourth argument (cr_no)

    # Print values (for debugging)
    print(f"Path: {path}")
    print(f"Server IP: {server_ip}")
    print(f"Server Port: {server_port}")
    print(f"CR No: {cr_no}")
          


    # Generate dynamic timestamp and query ID
    timestamp = generate_timestamp()
    query_id = generate_query_id()

    client_socket = connect_to_monitor(host, port)
    if not client_socket:
        return

    # Corrected QRY message with dynamic timestamp and query ID
    qry_message = (
        f"MSH|^~\\&|||||||QRY^R02|1203|P|2.3.1\r"
        f"QRD|{timestamp}|R|I|{query_id}|||||RES\r"
        f"QRF|MON||||0&0^1^1^0^101&160&161&200\r"
    )

    # Delay the query message by 10 seconds
    timer = threading.Timer(10, send_query_message, args=(client_socket, qry_message))
    timer.start()

    # Start TCP echo and data receiving
    running = threading.Event()
    running.set()
    try:
        # Run send_tcp_echo and receive_data in separate threads
        echo_thread = threading.Thread(target=send_tcp_echo, args=(client_socket, running))
        receive_thread = threading.Thread(target=receive_data, args=(client_socket, running))

        echo_thread.start()
        receive_thread.start()

        # Keep main thread alive
        while True:
            time.sleep(1)
    except KeyboardInterrupt:
        logging.info("Shutting down...")
        running.clear()
    finally:
        echo_thread.join()
        receive_thread.join()
        client_socket.close()
        logging.info("Connection closed.")

if __name__ == "__main__":
    main()
