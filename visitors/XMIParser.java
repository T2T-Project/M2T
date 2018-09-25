package ma.emi.est.mde.xmi.visitors;

import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import ma.emi.est.mde.xmi.abstraction.Activity;
import ma.emi.est.mde.xmi.abstraction.ControlFlow;
import ma.emi.est.mde.xmi.abstraction.Edge;
import ma.emi.est.mde.xmi.abstraction.Node;
import ma.emi.est.mde.xmi.abstraction.OpaqueAction;
import ma.emi.est.mde.xmi.abstraction.OpaqueExpression;


public class XMIParser extends Parser {

	private static String PKG = "ma.emi.est.mde.xmi.abstraction";

	private XMIParser() {
		
	}

	@Override
	public Object parse() throws Exception {
		
		// TODO Auto-generated method stub
		Object parsedObject = null;
		FileInputStream is = this.getSource();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document xmi = db.parse(is);

		xmi.getDocumentElement().normalize();

		NodeList packagedElements = xmi.getElementsByTagName("packagedElement");
		for (int i = 0; i < packagedElements.getLength(); i++) {
			Element e = (Element) packagedElements.item(i);

			if (e.getAttribute("xmi:type").equals("uml:Activity")) { /** Début de l'activité */
				Activity activity = new Activity();
				parsedObject = activity;

				activity.setId(e.getAttribute("xmi:id"));
				activity.setName(e.getAttribute("name"));

				NodeList activityElements = e.getChildNodes();

				//				*** début de la boucle
				for (int j = 0; j < activityElements.getLength(); j ++) {
					if (activityElements.item(j).getNodeType() == Document.ELEMENT_NODE) {
						Element f = (Element) activityElements.item(j);

				//	Préparation des noeuds ***********************************************************************************************					
						if (f.getTagName().equals("node")) {
							String nsType = f.getAttribute("xmi:type");
							String type = nsType.split(":")[1];
//							System.out.println("Type : " + type);

							Node node = (Node) Class.forName(PKG + "." + type).newInstance();
							node.setId(f.getAttribute("xmi:id"));
							node.setName(f.getAttribute("name"));

							switch (type) {
							case "OpaqueAction":
								OpaqueAction oa = (OpaqueAction) node;
								for (int k = 0; k < f.getChildNodes().getLength(); k++) {
									if (f.getChildNodes().item(k).getNodeType() == Document.ELEMENT_NODE) {
										Element g = (Element) f.getChildNodes().item(k);
										if (g.getTagName().equals("language")) {
											oa.setLanguage(g.getTextContent());
										} else if (g.getTagName().equals("body")) {
											oa.setBody(g.getTextContent());
										}
									}
								}
								activity.getNodes().add(oa);
								break;

							default:
								activity.getNodes().add(node);
								break;
							}

							//							Test de JAXB?? test effectué le 20/02/2017 23:48 --> Résultats: pas mal.
							//							

						}
						//	Préparations des arcs *****************************************************************************************************					
						if (f.getTagName().equals("edge")) {
							String nsType = f.getAttribute("xmi:type");
							String type = nsType.split(":")[1];
							//							System.out.println(type);

							Edge edge = (Edge) Class.forName(PKG + "." + type).newInstance();
							edge.setId(f.getAttribute("xmi:id"));
							edge.setName(f.getAttribute("name"));

							switch (type) {
							case "ControlFlow":
								ControlFlow cf = (ControlFlow) edge;
								if (f.hasChildNodes()) {
									for (int l = 0; l < f.getChildNodes().getLength(); l++) {
										if (f.getChildNodes().item(l).getNodeType() == Document.ELEMENT_NODE) {
											Element g = (Element) f.getChildNodes().item(l);
											if (g.getTagName().equals("guard")) {
												OpaqueExpression guard = new OpaqueExpression();
												guard.setId(g.getAttribute("xmi:id"));
												for (int m = 0; m < g.getChildNodes().getLength(); m++) {
													if (g.getChildNodes().item(m).getNodeType() == Document.ELEMENT_NODE) {
														Element h = (Element) g.getChildNodes().item(m);
														if (h.getTagName().equals("language")) {
															guard.setLanguage(h.getTextContent());
														} else if (h.getTagName().equals("body")) {
															guard.setBody(h.getTextContent());
														}
													}
												}
												cf.setGuard(guard);
											}
										}
									}
								}
								activity.getEdges().add(cf);
								break;
							default:
								activity.getEdges().add(edge);
								break;
							}
						}
					}

				} // *** Fin de la boucle 


				/**	1- Début du wiring des noeuds: *****/

				//				********* nodes wiring ********************
				int count = 0;
				for (int j = 0; j < activityElements.getLength(); j ++) {
					if (activityElements.item(j).getNodeType() == Document.ELEMENT_NODE) {

						Element f = (Element) activityElements.item(j);
						if (f.getTagName().equals("node")) {
							if (f.hasAttribute("incoming")) {
								String inEdges[] = f.getAttribute("incoming").split(" ");

								for (int k = 0; k < inEdges.length; k++) {
									for (int l = 0; l < activity.getEdges().size(); l++) {
										if (activity.getEdges().get(l).getId().equals(inEdges[k])) {
											Node current = activity.getNodes().get(count);
											current.getIncoming().add(activity.getEdges().get(l));
										}
									}
								}
							}

							if (f.hasAttribute("outgoing")) {
								String outEdges[] = f.getAttribute("outgoing").split(" ");	

								for (int k = 0; k < outEdges.length; k++) {
									for (int l = 0; l < activity.getEdges().size(); l++) {
										if (activity.getEdges().get(l).getId().equals(outEdges[k])) {
											activity.getNodes().get(count).getOutgoing().add(activity.getEdges().get(l));

										}
									}
								}
							}
						}

						//				*********************************
						count ++;
					}
				} /** Fin du wiring des noeuds *********/
				
			} /** fin de l'activité */
		}
		return parsedObject;
	}

	public static Parser newParser() {
		if (parser == null) {
			parser = new XMIParser();
		}

		return parser;
	}

}
