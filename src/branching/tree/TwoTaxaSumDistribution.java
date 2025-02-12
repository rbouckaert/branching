package branching.tree;

import java.util.List;
import java.util.Random;

import beast.base.core.Description;
import beast.base.core.Input;
import beast.base.core.Input.Validate;
import beast.base.inference.Distribution;
import beast.base.inference.State;
import beast.base.inference.parameter.RealParameter;

@Description("Branching process likelihood for 2 taxa by infinite sum")
public class TwoTaxaSumDistribution extends Distribution {
	public Input<RealParameter> treeHeightInput = new Input<>("treeHeight", "height of the 2 taxon tree", Validate.REQUIRED);

	RealParameter treeHeight;

	@Override
	public void initAndValidate() {
		treeHeight = treeHeightInput.get();
	}
	
	@Override
	public double calculateLogP() {
		logP = 0;
		double t = treeHeight.getArrayValue();
//		for (int i = 0; i < 100; i++) {
//			logP += 2.0 * i * Math.pow(t, i-1)/((i+2.0)*(i+1.0));
//		}
		
		logP = -2.0*(1.0/(t*t)) * (2.0 + Math.log(1.0-t) * (2.0-t)/t);
		logP = Math.log(logP);
		return logP;
	}
	
	
	@Override
	public List<String> getArguments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getConditions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sample(State state, Random random) {
		// TODO Auto-generated method stub

	}

}
