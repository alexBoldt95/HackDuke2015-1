import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.*;

public class DrugScanner 
{	
	//stores each collection(set) of similar drugs
	public static ArrayList<Node> myNodeSets = new ArrayList<Node>();
	
	//Builds a TreeMap which stores generic names with brand names as keys
	public TreeMap<String,String> brandGenericMap(){
		TreeMap<String,String> brandToGeneric = new TreeMap<String,String>();
		for(int i = 0; i < myNodeSets.size(); i++){
			TreeSet<String> tempBrands = myNodeSets.get(i);
			ArrayList<String> alBrands = new ArrayList<String>();
			alBrands.addAll(tempBrands);
			for(int j = 1; j < tempBrands.size(); j++){
				brandToGeneric.put(alBrands.get(j),alBrands.get(0));
			}
		}
		return brandToGeneric;
	}
	
	//Builds a TreeMap which stores Nodes with generic names as keys
	public TreeMap<String,Node> genericToNodeMap(){
		TreeMap<String,Node> genericToNode = new TreeMap<String,Node>();
		for(int i = 0; i < myNodeSets.size(); i++){
			Node tempNode = myNodeSets.get(i);
			ArrayList<String> tempBrands = new ArrayList<String>();
			tempBrands.addAll(tempNode.brandNames);
			String generic = tempBrands.get(0);
			genericToNode(generic,tempNode);
		}
		return genericToNode;
	}
	
	public DrugScanner(String fileName){
		ArrayList<Node> myNodeSets = new ArrayList<Node>();
		scanTheFile(File fileName);
		TreeMap<String,String> brandToGeneric = brandToGenericMap();
		TreeMap<String,Node> genericToNode = genericToNodeMap();
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
		File textFile = new File("data/Medicine.txt");
		scanTheFile(textFile);
		testArray();
	}

	public static void scanTheFile(File textFile)
	{
		//use these fillers to make the variables of the Node class later
		TreeSet<String> currentBrandSet = new TreeSet<String>();
		TreeSet<String> currentAlternativeSet = new TreeSet<String>();
		String currentLine = "";
		String currentGeneric = "";

		//need instance of DrugScanner
		//DrugScanner myScanner = new DrugScanner();

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
							DrugScanner.myNodeSets.add(new Node( currentGeneric, new TreeSet<String>(currentBrandSet), 
									new TreeSet<String>(currentAlternativeSet)));
							currentBrandSet.clear();
							currentAlternativeSet.clear();
						}
					}
				}
			}
		} 

		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}

	}

	public static void testArray()
	{
		for(Node drug: myNodeSets){
			System.out.println(drug.genericName);
			System.out.println(drug.brandNames.toString());
			System.out.println(drug.alternatives.toString());
		}
	}
	public static Node genericLookUp(String theGeneric)
	{
		return genericToNode.get(theGeneric);
	}

	public static Node brandLookup(String theBrand)
	{
		String generic = brandToGeneric.get(theBrand);
		return genericToNode.get(generic);
	}
}
