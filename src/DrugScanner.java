import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.*;

public class DrugScanner 
{	
	//stores each collection(set) of similar drugs
	public ArrayList<Node> myNodeList;
	TreeMap<String,String> brandToGeneric;
	TreeMap<String,Node> genericToNode;

	//Builds a TreeMap which stores generic names with brand names as keys
	public void brandToGenericMap(){
		brandToGeneric = new TreeMap<String, String>();
		for(int i = 0; i < myNodeList.size(); i++){
			String generic = myNodeList.get(i).genericName;
			TreeSet<String> tempBrands = myNodeList.get(i).brandNames;
			for(String brand:tempBrands){
				brandToGeneric.put(brand, generic);
			}
		}
	}

	//Builds a TreeMap which stores Nodes with generic names as keys
	public void genericToNodeMap(){
		genericToNode = new TreeMap<String, Node>();
		for(int i = 0; i < myNodeList.size(); i++){
			Node tempNode = myNodeList.get(i);
			String generic = myNodeList.get(i).genericName;
			genericToNode.put(generic,tempNode);
		}
	}

	public DrugScanner(String fileName){
		myNodeList = new ArrayList<Node>();
		File toScan = new File(fileName);
		scanTheFile(toScan);
		brandToGenericMap();
		genericToNodeMap();
	}

	//each drug is going to be its own node
	public static class Node
	{
		String genericName;
		TreeSet<String> brandNames;
		TreeSet<String> alternatives;

		public Node()
		{
			genericName = "";
			brandNames = null;
			alternatives = null;
		}

		public Node(String myGeneric, TreeSet<String> myBrandNames, TreeSet<String> myAlternatives)
		{
			genericName = myGeneric;
			brandNames = myBrandNames;
			alternatives = myAlternatives;
		}
	}

	public static void main(String[] args)
	{
		DrugScanner t = new DrugScanner("data/Medicine.txt");
		System.out.println(t.brandToGeneric.toString());
		System.out.println(t.genericToNode.toString());

	}

	public void scanTheFile(File textFile)
	{
		//use these fillers to make the variables of the Node class later
		TreeSet<String> currentBrandSet = new TreeSet<String>();
		TreeSet<String> currentAlternativeSet = new TreeSet<String>();
		String currentLine = "";
		String currentGeneric = "";

		try 
		{
			Scanner sc = new Scanner(textFile);

			if( sc.hasNextLine())
			{

				currentLine = sc.nextLine();

			}

			while( sc.hasNextLine())
			{	
				if(currentLine.isEmpty())
				{

					if(sc.hasNextLine())
					{
						currentGeneric = sc.nextLine();
						//System.out.println(currentGeneric);
						currentLine = sc.nextLine();
						//System.out.println(currentLine);

						//brand loop
						while( !currentLine.isEmpty() && currentLine.indexOf('*') < 0)
						{
							currentBrandSet.add(currentLine);
							currentLine = sc.nextLine();
						}

						//alternatives loop
						if( currentLine.indexOf('*') > -1)
						{
							currentLine = sc.nextLine();
							while(!currentLine.isEmpty())
							{
								//System.out.println(currentLine);
								currentAlternativeSet.add(currentLine);
								currentLine = sc.nextLine(); 
							}
						}

						if( currentLine.isEmpty())
						{
							//System.out.println(currentGeneric);
							myNodeList.add(new Node( currentGeneric, new TreeSet<String>(currentBrandSet), 
									new TreeSet<String>(currentAlternativeSet)));
							currentBrandSet.clear();
							currentAlternativeSet.clear();
						}
					}
				}
				sc.close();
			}
		}


		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}

	}

	public void testArray()
	{
		//		for(Node drug: myNodeSets){
		//			System.out.println(drug.genericName);
		//			System.out.println(drug.brandNames.toString());
		//			System.out.println(drug.alternatives.toString());
		//		}

	}
	public Node genericLookUp(String theGeneric)
	{
		return genericToNode.get(theGeneric);
	}

	public Node brandLookup(String theBrand)
	{
		String generic = brandToGeneric.get(theBrand);
		return genericToNode.get(generic);
	}
}
