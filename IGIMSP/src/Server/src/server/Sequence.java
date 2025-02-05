package server;

public class Sequence
{
	int i = 0;

	public Integer next()
	{
		return new Integer(++i);
	}
}
