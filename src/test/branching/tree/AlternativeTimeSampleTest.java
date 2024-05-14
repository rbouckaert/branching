package test.branching.tree;

import java.io.PrintStream;
import java.util.Arrays;

import beast.base.util.Randomizer;

public class AlternativeTimeSampleTest {

	public static void main(String[] args) throws Exception {
		PrintStream out = new PrintStream("/tmp/sample.log");
		out.print("Sample\t");
		int N = 9-4;
		for (int j = 0; j < N; j++) {
			out.print("internalNodeTime." + j + "\t");
		}
		out.println();
		
		double [] times = new double[N + 1];
		for (int i = 0; i < 10000; i++) {
			out.print(i+"\t");
			for (int j = 0; j < N + 1; j++) {
				times[j] = Randomizer.nextDouble();
				times[j] = times[j] / (1-times[j]);
			}
			
			Arrays.sort(times);
			for (int j = 0; j < N; j++) {
				out.print(times[j]/times[N] + "\t");
			}
			out.println();
		}

		out.close();
		System.err.println("Done");
	}

}
