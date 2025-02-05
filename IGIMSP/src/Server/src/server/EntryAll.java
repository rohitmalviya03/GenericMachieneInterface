package server;

import java.io.Serializable;
import java.util.Comparator;

public class EntryAll implements Serializable, Cloneable
{
	private String value1 = "";
	private String value2 = "";
	private String value3 = "";
	private String value4 = "";
	private String value5 = "";

	public EntryAll(String _Value1,String _Value2,String _Value3,String _Value4,String _Value5)
	{
		
		this.value1 = _Value1;
		this.value2 = _Value2;
		this.value3 = _Value3;
		this.value4 = _Value4;
		this.value5 = _Value5;
	}

	public EntryAll()
	{
	}

	

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public String getValue3() {
		return value3;
	}

	public void setValue3(String value3) {
		this.value3 = value3;
	}

	public String getValue4() {
		return value4;
	}

	public void setValue4(String value4) {
		this.value4 = value4;
	}

	public String getValue5() {
		return value5;
	}

	public void setValue5(String value5) {
		this.value5 = value5;
	}

	public String toString()
	{
		return ("[" + this.value1 + ", " + this.value2 +", " + this.value3 +", " + this.value4 +"," + this.value5 + "]");
	}

	public boolean equals(Object obj)
	{
		if (obj == this) // (2)
		return true;
		if (obj == null || !(obj instanceof EntryAll)) // (3)
		return false;
		EntryAll objEntry1 = (EntryAll) obj;
		EntryAll objEntry2 = (EntryAll) obj;
		EntryAll objEntry3 = (EntryAll) obj;
		EntryAll objEntry4 = (EntryAll) obj;
		EntryAll objEntry5 = (EntryAll) obj;
		return (objEntry1.value1 == null ? false : objEntry1.value1.equals(this.value1));
		/*EntryAll objEntry2 = (EntryAll) obj;
		return (objEntry2.value2 == null ? false : objEntry2.value2.equals(this.value2));
		EntryAll objEntry3 = (EntryAll) obj;
		return (objEntry3.value3 == null ? false : objEntry3.value3.equals(this.value3));
		EntryAll objEntry4 = (EntryAll) obj;
		return (objEntry4.value4 == null ? false : objEntry4.value4.equals(this.value4));*/
		
	}

	public int hashCode()
	{

		int hashValue = 0;
		hashValue = this.value1.hashCode();
		return hashValue;
	}

	public static class EntryComparator implements Comparator
	{
		public int compare(Object obj1, Object obj2)
		{

			EntryAll objEntry1 = ((EntryAll) obj1);
			EntryAll objEntry2 = ((EntryAll) obj2);
			EntryAll objEntry3 = ((EntryAll) obj2);
			EntryAll objEntry4 = ((EntryAll) obj2);
			EntryAll objEntry5 = ((EntryAll) obj2);
			return objEntry1.getValue1().compareTo(objEntry2.getValue1());
		}

	}

	public Object clone()
	{
		Object obj = null;
		try
		{
			obj = super.clone();
		}
		catch (CloneNotSupportedException e)
		{

		}
		return obj;

	}
}
