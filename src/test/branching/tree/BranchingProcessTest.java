package test.branching.tree;

import java.io.PrintStream;

import beast.base.evolution.alignment.Alignment;
import beast.base.evolution.alignment.Sequence;
import beast.base.evolution.tree.Node;
import beast.base.evolution.tree.Tree;
import beast.base.evolution.tree.TreeParser;
import branching.tree.BranchingProcess;

public class BranchingProcessTest {

    public static Alignment getFourTaxaNoData() throws Exception {
        Sequence a = new Sequence("A", "A");
        Sequence b = new Sequence("B", "A");
        Sequence c = new Sequence("C", "A");

        Alignment data = new Alignment();
        data.initByName("sequence", a, "sequence", b, "sequence", c, "dataType", "nucleotide");
        return data;
    }

    static public Tree getTree(Alignment data, String tree) throws Exception {
        TreeParser t = new TreeParser();
        t.initByName("taxa", data,
                "newick", tree,
                "IsLabelledNewick", true);
        return t;
    }

    public static void main(String[] args) throws Exception {
		Alignment data = getFourTaxaNoData();
		Tree tree = getTree(data, "((A:0.25,B:0.25):0.25,C:0.5)");
		
		BranchingProcess p = new BranchingProcess();
		p.initByName("tree", tree, "origin", "1.0", "beta", "1.0");
		
		Node node1 = tree.getRoot();
		Node node2 = tree.getNode(3);
		
		double N = 100;
		
		PrintStream out = new PrintStream("/tmp/BranchingProcessTest.dat");
		for (int i = 1; i < N; i++) {
			for (int j = 1; j < N; j++) {
				node1.setHeight(i/N);
				node2.setHeight((j+0.01)/N);
				double logP = p.calculateLogP();
				out.print(logP + " ");
			}
			out.println();
		}
		out.close();
		System.err.println("Done");

	}

}
