package ma.emi.est.mde.xmi.visitors;

import ma.emi.est.mde.xmi.abstraction.Activity;
import ma.emi.est.mde.xmi.abstraction.ActivityFinalNode;
import ma.emi.est.mde.xmi.abstraction.DecisionNode;
import ma.emi.est.mde.xmi.abstraction.InitialNode;
import ma.emi.est.mde.xmi.abstraction.OpaqueAction;

public abstract class XMIVisitor {

	/** La m�thode visite n'est pas abstraite expr�s pour permettre de visiter n'importe quoi. ***/
	
	public boolean visit(Object o) {
		System.out.println("Visit: " + o.getClass().getName());
		return false;
	}
	
	public boolean visit(InitialNode o) {
		return false;
	}
	
	/** Ajouter endVisit() pour chaque m�thode ***/
	
	public boolean visit(DecisionNode o) {
		
		return false;
	}
	
	public boolean visit(OpaqueAction o) {
	
		return false;
	}
	
	public boolean visit(ActivityFinalNode o) {
	
		return false;
	}

	public boolean visit(Activity o) {
		
		return false;
	}

	public boolean endVisit(Activity o) {
		
		return false;
	}
}
