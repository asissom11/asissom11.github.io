
public class Cat extends Pet
{
	private int mCatSpaceNumber;
	
	public Cat(String aCatName, int aAge)
	{
		super(aCatName, aAge, "Cat");
	}
	
	public int getCatSpaceNumber()
	{
		return this.mCatSpaceNumber;
	}
	
	public void setCatSpaceNumber(int aNumber)
	{
		this.mCatSpaceNumber = aNumber;
	}
}
