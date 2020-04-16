package tw.edu.nsysu.cse.iccsl.dnet;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class PBCPolynomial {
	
	private static Pairing pairing		= null;
	@SuppressWarnings("rawtypes")
	private static Field zr				= null;	
	private Element [] p1 	= null;
	
	PBCPolynomial(){
		pairing = PairingFactory.getPairing("curves/a.properties");
		zr = pairing.getZr();
		p1 = new Element[1];
		p1[0] = zr.newOneElement();
	}

	public Element[] getPolynomial(int thisTypeValNum) {
		Element[] p = new Element[thisTypeValNum+1];
		if(p1.length==1) {
			for(int i=0;i<p.length;i++) {
				p[i]=zr.newZeroElement();
			}
		}else {
			for(int i=0;i<p.length;i++) {
				if(i<p.length-p1.length) {
					p[i]=zr.newZeroElement();
				}else {
					p[i]=p1[i-(p.length-p1.length)];
				}
			}
		}
		return p;
	}

	public void mul(Element[] p2) {
		Element[] p = new Element[p1.length+p2.length-1];
		for(int i=0;i<p.length;i++) {
			p[i] = zr.newZeroElement();
		}
		for(int i=0;i<p1.length;i++) {
			for(int j=0;j<p2.length;j++) {
				int d = i + j;
				p[d].add(p1[i].getImmutable().mul(p2[j]));
			}
		}
		p1 = p;
	}

	

}
