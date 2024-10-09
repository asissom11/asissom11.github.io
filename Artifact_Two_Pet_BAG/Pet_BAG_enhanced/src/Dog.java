
public class Dog extends Pet
{
	public int mDogSpaceNumber;
	private double mDogWeightInPounds;
	private boolean mGroomingAnimal;
	
	public Dog (String aDogName, int aAge)
	{
		super(aDogName, aAge, "Dog");
	}
	
	public double getDogWeightInPounds()
	{
		return this.mDogWeightInPounds;
	}
	
	public boolean getGroomingAnimal()
	{
		return this.mGroomingAnimal;
	}
	
	public void setDogWeightInPounds(double aWeight)
	{
		this.mDogWeightInPounds = aWeight;
	}
	
	public void setGroomingAnimal(boolean aValue)
	{
		this.mGroomingAnimal = aValue;
	}
}
