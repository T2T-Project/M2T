package ma.emi.est.mde.xmi.visitors;

import java.util.Scanner;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeParameter;

import ma.emi.est.mde.xmi.abstraction.Activity;
import ma.emi.est.mde.xmi.abstraction.ActivityFinalNode;
import ma.emi.est.mde.xmi.abstraction.DecisionNode;
import ma.emi.est.mde.xmi.abstraction.InitialNode;
import ma.emi.est.mde.xmi.abstraction.OpaqueAction;

public class ActivityDiagramVisitor extends XMIVisitor {

	private CompilationUnit unit;
	private AST astUnit;
	private TypeDeclaration class_;
	private MethodDeclaration method;
	private Block block;

	@SuppressWarnings("unchecked")
	public ActivityDiagramVisitor(CompilationUnit unit) {
		super();
		this.setUnit(unit);

		astUnit = unit.getAST();

		/**		Déclaration de la class_ contenante de la méthode transformée ***/
		class_ = astUnit.newTypeDeclaration();
		class_.modifiers().add(astUnit.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));
		class_.setName(astUnit.newSimpleName("Test"));

		/** 		************************************				***/

	}

	public CompilationUnit getUnit() {
		return unit;
	}

	public void setUnit(CompilationUnit unit) {
		this.unit = unit;
	}

	@SuppressWarnings("unchecked")
	public boolean visit(Activity o) {
		
		method = astUnit.newMethodDeclaration();
		method.setConstructor(false);

		method.modifiers().add(astUnit.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));
		method.setName(astUnit.newSimpleName(o.getName()));
		
		
		method.setReturnType2(astUnit.newPrimitiveType(PrimitiveType.VOID));

		for (int i = 0; i < o.getNodes().size(); i++) {
			o.getNodes().get(i).accept(this);
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean endVisit(Activity o) {
		// TODO Auto-generated method stub
		class_.bodyDeclarations().add(method);
		unit.types().add(class_);

		return false;
	}

	public boolean visit(InitialNode o) {
		block = astUnit.newBlock();

		return false;
	}

	@SuppressWarnings("unchecked")
	
	public boolean visit(OpaqueAction o) {
		//		System.out.println("OpaqueAction visited");
		if (o.getLanguage().equals("JAVA")) {
			ASTParser subparser = ASTParser.newParser(AST.JLS3);
			Expression e = null;
			ExpressionStatement exp = null;
			
			try {
				subparser.setKind(ASTParser.K_STATEMENTS);
				subparser.setSource(o.getBody().toCharArray());
				Block action = (Block) subparser.createAST(null);	
				
				exp = (ExpressionStatement) action.statements().get(0);
				e = exp.getExpression();
	
			} catch (IndexOutOfBoundsException e1) {
				try {
					subparser.setKind(ASTParser.K_EXPRESSION);
					subparser.setSource(o.getBody().toCharArray());
					
					InfixExpression exp2 = (InfixExpression) subparser.createAST(null);	
					e = exp2;
				} catch (ClassCastException e2) {
					// TODO: handle exception
				}
				
			}
			
			if (e instanceof MethodInvocation) {
				MethodInvocation m = (MethodInvocation) exp.getExpression();
				MethodInvocation i = (MethodInvocation) ASTNode.copySubtree(astUnit, m);
				i.delete();

			} else if (e instanceof InfixExpression) {
				System.out.println("operation.");
			}

		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public boolean visit(DecisionNode o) {
	
		AST ast = block.getAST();

		IfStatement ifStatement = ast.newIfStatement();
		
		String condition = o.getCondition().getBody();
		ASTParser subparser = ASTParser.newParser(AST.JLS3);
		subparser.setKind(ASTParser.K_EXPRESSION);
		subparser.setSource(condition.toCharArray());
		
		InfixExpression expression = (InfixExpression) subparser.createAST(null);	
		InfixExpression i = (InfixExpression) ASTNode.copySubtree(astUnit, expression);
		i.delete();
		ifStatement.setExpression(i);

		block.statements().add(ifStatement);

		return false;
	}

	public boolean visit(ActivityFinalNode o) {

		method.setBody(block);
		
		return false;
	}
	
}

