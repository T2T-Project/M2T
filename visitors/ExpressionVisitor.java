package ma.emi.est.mde.xmi.visitors;

import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.text.edits.TextEdit;

public class ExpressionVisitor extends ASTVisitor {

	private Block block;
	
	public ExpressionVisitor(Block block) {
		// TODO Auto-generated constructor stub
		this.block = block;
	}

	@SuppressWarnings({ "unchecked", "static-access" })
	@Override
	public boolean visit(MethodInvocation methodInvocation) {
		System.out.println("***");
		
		System.out.println("***");
		// TODO Auto-generated method stub		
		
		return false;
		
	}

}
