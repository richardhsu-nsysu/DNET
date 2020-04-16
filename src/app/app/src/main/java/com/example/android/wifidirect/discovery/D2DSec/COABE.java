package com.example.android.wifidirect.discovery.D2DSec;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Random;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;


public class COABE implements Serializable{
    private String mCurveFilePath	= null;
    private Pairing 	mPairing 		= null;
    public static Field 		mZr 			= null;
    public static Field		mG1				= null;
    public static Field		mG2				= null;
    public static Field		mGT				= null;

    int 				mMode			= -1;	//0 = KGC mode
    //1 = user mode

    public static int KGC_MODE			= 0;
    public static int USER_MODE  		= 1;

    private Element[] mMasterSecret 	= new Element[5];	//mMasterSecret[0] = \bar{A}=g^{\alpha}
    //mMasterSecret[1] = \alpha
    //mMasterSecret[2] = \alpha'
    //mMasterSecret[3] = \beta'
    //mMasterSecret[4] = \delta
    private Element barA 				= null; 			//e(g,g)^{\alpha}
    private Element alpha				= null;				//\alpha
    private Element alphap				= null;				//\alpha'
    private Element betap				= null;				//\beta'
    private Element delta				= null;				//\delta


    private Element[] mPublicParameters = null; 			//mPublicParameters[0] = g
    //mPublicParameters[1] = \tilde{A}=e(g,g)^{\alpha}
    //mPublicParameters[2] = h_0
    //mPublicParameters[3] = h
    //mPublicParameters[4] = u
    //mPublicParameters[5] = v
    //mPublicParameters[6] = u^{\alpha'}
    //mPublicParameters[7] = v^{\beta'}
    //mPublicParameters[8] = \Delta=g^{\delta}
    //mPublicParameters[9]~[8+mNumAttributeType] = \vec{H}
    private Element g 					= null;				//g
    private Element tildeA 				= null;				//\tilde{A}
    private Element h_0 				= null;				//h_0
    private Element h					= null;				//h
    private Element u					= null;				//u
    private Element v 					= null; 			//v
    private Element u_ap				= null;				//u^{\alpha'}
    private Element v_bp				= null;				//v^{\beta'}
    private Element Delta				= null;				//\Delta
    private Element[] vecH				= null;				//\vec{H}

    private int mNumAttributeType 		= 50;				//the number of attribute type is set to 50 as default value



    private Element[] 	mCoSecret 		= new Element[2];	//mCoSecret[0] = x'
    //mCoSecret[1] = y'
    private Element xp					= null;				//x'
    private Element	yp					= null;				//y'

    //private int mNumAttributeType 		= 50;				//the number of attribute type is set to 50 as default value


    private Element s 					= null;
    private Element sp 					= null;
    private Element r 					= null;


    //user private key for user mode
    private Element[]	mSK				= null;

    public COABE(int mode, String curve_file_path) {
        mCurveFilePath = curve_file_path;
        genPairingParameters();
    }

    //*************************************************************************************************
    //the following methods are for individual users who
    //have their own private keys, respectively
    //*************************************************************************************************

    //setup public parameters for CORE algorithm
    public void setPublicParams (Element[] pub_params, int num_att) {
        mPublicParameters = pub_params;
        g 		= pub_params[0];
        tildeA 	= pub_params[1];
        h_0 	= pub_params[2];
        h		= pub_params[3];
        u 		= pub_params[4];
        v 		= pub_params[5];
        u_ap    = pub_params[6];
        v_bp	= pub_params[7];
        Delta 	= pub_params[8];


        for (int i = 0; i < num_att; i++) {
            vecH[i] = pub_params[9+i];
        }

        return;
    }

    //setup individual private key
    public void setPrivateKey (Element[] sk) {
        mSK = sk;
        return;
    }

    //*************************************************************************************************
    //the following methods are for the key generation
    //center (KGC),which is responsible for the key
    //issuing
    //*************************************************************************************************

    public void setMasterKey (Element[] master_secret, Element[] csk) {
        barA 	= master_secret[0] ;
        alpha	= master_secret[1] ;
        alphap  = master_secret[2] ;
        betap 	= master_secret[3] ;
        delta	= master_secret[4] ;

        xp 		= csk[0];
        yp		= csk[1];
        return;
    }

    public void setNumAttType(int num_att) {
        mNumAttributeType = num_att;
    }
    //*************************************************************************************************
    //setup method is to generate the required public parameters and the corresponding
    //master secret keys for the CORE system
    //for system adminstrator only
    //*************************************************************************************************
    public boolean setup (int sec_param) {
        alpha 	= mZr.newRandomElement();	//\alpha
        alphap 	= mZr.newRandomElement();	//\alpha'
        betap 	= mZr.newRandomElement();	//\beta'
        delta 	= mZr.newRandomElement();	//delta

        xp		= mZr.newRandomElement();	//x'
        yp		= mZr.newRandomElement();	//y'

        g 		= mG1.newRandomElement();	//g
        barA 	= g.getImmutable().powZn(alpha.getImmutable()); //\bar{A} = g^{\alpha}
        //\tilde{A}
        tildeA 	= mPairing.pairing(g.getImmutable(),g.getImmutable()).getImmutable().powZn(alpha.getImmutable());
        h_0		= mG1.newRandomElement();
        Element[] vec_alpha = new Element[mNumAttributeType];
        vecH				= new Element[mNumAttributeType];


        for (int i = 0; i < mNumAttributeType; i++) {
            vec_alpha[i] = mZr.newRandomElement();
            vecH[i] = g.getImmutable().powZn(vec_alpha[i].getImmutable());
        }

        h = mG1.newRandomElement();
        //generate u and v such that u^x'=v^y'=h
        u = h.getImmutable().powZn(xp.getImmutable().invert());
        v = h.getImmutable().powZn(yp.getImmutable().invert());

        u_ap = u.getImmutable().powZn(alphap.getImmutable()); 	//u^{\alpha}'
        v_bp = v.getImmutable().powZn(betap.getImmutable());	//v^{\beta'}
        Delta = g.getImmutable().powZn(delta.getImmutable());


        //;mPublicParameters = pub_params;
        mPublicParameters 	 = new Element[9+mNumAttributeType];
        mPublicParameters[0] = g 		;
        mPublicParameters[1] = tildeA 	;
        mPublicParameters[2] = h_0 	    ;
        mPublicParameters[3] = h		;
        mPublicParameters[4] = u 		;
        mPublicParameters[5] = v 		;
        mPublicParameters[6] = u_ap     ;
        mPublicParameters[7] = v_bp	    ;
        mPublicParameters[8] = Delta 	;


        for (int i = 0; i < mNumAttributeType; i++) {
            mPublicParameters[9+i] = vecH[i];
        }


        mMasterSecret[0] = barA 	;
        mMasterSecret[1] = alpha	;
        mMasterSecret[2] = alphap   ;
        mMasterSecret[3] = betap 	;
        mMasterSecret[4] = delta	;

        mCoSecret[0] = xp;
        mCoSecret[1] = yp;
        return true;
    }

    //*************************************************************************************************
    // getPubParam(): return Element[], get public parameters of the COABE system
    //*************************************************************************************************
    public Element[] getPubParam() {
        return mPublicParameters;
    }

    //*************************************************************************************************
    // getMasterSecret(): return Element[], get master secret of the COABE system
    //*************************************************************************************************
    public Element[] getMasterSecret() {
        return mMasterSecret;
    }

    //*************************************************************************************************
    // getCoSecret(): return Element[], get cooperative secret of the COABE system
    //*************************************************************************************************
    public Element[] getCoSecret() {
        return mCoSecret;
    }

    //*************************************************************************************************
    //genKey method is to generate a user private key for the given attributes
    //this method is only for KGC =
    //return sk: sk[0] = D_1, sk[1] = D_2, sk[2~1+n] = K[0~n-1]
    //*************************************************************************************************
    public Element[] genKey(Element[] att_set) {
        int attr_len 	= att_set.length;
        r       		= mZr.newRandomElement();

        Element D1 = ( (g.getImmutable().powZn(alpha.getImmutable())).mul(h_0.getImmutable().powZn(r.getImmutable())) ).mul( h.getImmutable().powZn( (alphap.getImmutable().add(betap.getImmutable())).getImmutable().mul(delta.getImmutable()) ) );
        Element D2 = g.getImmutable().powZn(r.getImmutable());

        Element[] sk = new Element[mNumAttributeType+1]; //D_1, D_2, K_i for i = 2 to n, n+1 elements in total

        sk[0] = D1;
        sk[1] = D2;

        System.out.println("####COABE.genKey(): mNumAttributeType="+mNumAttributeType);

        Element[] K = new Element[mNumAttributeType];
        for (int i=0; i < mNumAttributeType-1; i++) {
            //sk[2+i] = (vecH[0].getImmutable().powZn( (att_set[i+1].getImmutable().invert()).div(att_set[0].getImmutable()) ).mul(vecH[i+1].getImmutable()) ).powZn(r.getImmutable());
            sk[2+i] = (  vecH[0].getImmutable().powZn( (att_set[i+1].getImmutable().negate()).div(att_set[0].getImmutable()) ).mul(vecH[i+1].getImmutable()) ).powZn(r.getImmutable());
            //System.out.println(i+"----"+sk[2+i]);
        }
        return sk;
    }

    //*************************************************************************************************
    //PreEnc is performed by the encryptor itself
    //return element[] PC: PC[0] = C_1, PC[1] = C_2, PC[2] = C_3, PC[3] = e_1, PC[4] = e_2
    //*************************************************************************************************
    public Element[] PreEnc(Element[] policy_set, Element M) {
        s            = mZr.newRandomElement();
        Element[] PC = new Element[5];
        PC[0] = g.getImmutable().powZn(s.getImmutable()); //C_1 = g^s

        //(h_0xh_1^{y_1}x...xh_n^{y_n})^s
        Element tmp = h_0.getImmutable();
        for (int i=0;i<mNumAttributeType;i++) {
            tmp = tmp.getImmutable().mul( vecH[i].getImmutable().powZn(policy_set[i].getImmutable()) );
        }
        PC[1] = tmp.getImmutable().powZn(s.getImmutable()); //C_2
        PC[2] = M.getImmutable().mul( mPairing.pairing(g.getImmutable(),g.getImmutable()).powZn( alpha.getImmutable().mul(s.getImmutable()) ) ); //C_3

        PC[3] = ( u.getImmutable().powZn(alphap.getImmutable()) ).getImmutable().powZn(s.getImmutable()); //e_1
        PC[4] = ( v.getImmutable().powZn(betap.getImmutable()) ).getImmutable().powZn(s.getImmutable()); //e_2

        return PC;

    }


    //*************************************************************************************************
    //CoEnc is performed by the cooperator device
    //return element[] C: C[0] = C_1', C[1] = C_2', C[2] = C_3', C[3] = e_1', C[4] = e_2'
    //*************************************************************************************************
    public Element[] CoEnc(Element[] PC, Element[] policy_set) {
        Element[] C = new Element[5];
        sp          = mZr.newRandomElement();	//select s' at random
        C[0] = PC[0].getImmutable().mul( g.getImmutable().powZn(sp.getImmutable()) );

        //(h_0xh_1^{y_1}x...xh_n^{y_n})^s'
        Element tmp = h_0.getImmutable();
        for (int i=0;i<mNumAttributeType;i++) {
            tmp = tmp.getImmutable().mul( vecH[i].getImmutable().powZn(policy_set[i].getImmutable()) );
        }
        C[1] = PC[1].getImmutable().mul( tmp.getImmutable().powZn(sp.getImmutable()) );
        C[2] = PC[2].getImmutable().mul( mPairing.pairing(g.getImmutable(),g.getImmutable()).getImmutable().powZn( alpha.getImmutable().mul(sp.getImmutable()) ) );
        C[3] = ( PC[3].getImmutable().mul(
                ( u.getImmutable().powZn(alphap.getImmutable()) ).getImmutable().powZn(sp.getImmutable()) ) ).getImmutable().powZn(xp.getImmutable());
        C[4] = ( PC[4].getImmutable().mul(
                ( v.getImmutable().powZn(betap.getImmutable()) ).getImmutable().powZn(sp.getImmutable()) ) ).getImmutable().powZn(yp.getImmutable());

        return C;
    }

    //*************************************************************************************************
    //Dec is to decrypt the given ciphertext
    //input sk: element sk[0] = D_1, sk[1] = D_2, sk[2~1+n] = K[0~n-1],
    //          element[] C: C[0] = C_1', C[1] = C_2', C[2] = C_3', C[3] = e_1', C[4] = e_2'
    //			element[] policy_set = y_1,....,y_n
    //return M
    //*************************************************************************************************
    public Element Dec(Element[] sk, Element[] C, Element[] policy_set) {
        Element tmp = sk[0].getImmutable();
        for (int i=0; i < mNumAttributeType-1; i++) {
            tmp = tmp.getImmutable().mul( sk[2+i].getImmutable().powZn(policy_set[i+1].getImmutable()) );
        }

        Element part_1 = mPairing.pairing(C[0].getImmutable(), tmp.getImmutable());
        Element part_2 = mPairing.pairing(C[1].getImmutable(),sk[1].getImmutable()).mul(
                mPairing.pairing(Delta.getImmutable(), C[3].getImmutable().mul(C[4].getImmutable())) );
        Element Gamma = part_1.getImmutable().div(part_2.getImmutable());

        Element M = C[2].getImmutable().div(Gamma.getImmutable());

        return M;
    }


    // this function is for testing...
    public void test (Element[] PC, Element[] C, Element[] sk, Element M, Element[] att_set, Element[] policy_set) {

        //test C_3'/e(g,g)^{\alpha*(s+s')}
        System.out.println("####test() - M=" + M);

        Element Mp = C[2].getImmutable().div(  mPairing.pairing(g.getImmutable(),g.getImmutable()).powZn( alpha.getImmutable().mul(s.getImmutable().add(sp.getImmutable())) )  );

        System.out.println("####test() - Mp=" + Mp);

        Element result0 = Mp.getImmutable().div(M.getImmutable());

        System.out.println("####test() - result0="+result0);

        //test e(C'_1,h_0^{r}*K_2^{y_2}*...*K_n^{y_n})/e(C_2',D_2)
        Element tmp = h_0.getImmutable().powZn(r.getImmutable());
        for (int i=0; i < mNumAttributeType-1; i++) {
            tmp = tmp.getImmutable().mul( sk[2+i].getImmutable().powZn(policy_set[i+1].getImmutable()) );
        }
        Element result = mPairing.pairing( C[0].getImmutable(),  tmp).getImmutable().div( mPairing.pairing(C[1].getImmutable(),sk[1].getImmutable()) );

        System.out.println("####test() - result = " + result);

        //test K_2^{y_2}...K_n^{y_n} = (h_1^{y_1}h_2^{y_2}...h_n^{y_n})^r
        Element tmp1 = mG2.newElement().setToOne();
        for (int i=0; i < mNumAttributeType-1; i++) {
            tmp1 = tmp1.getImmutable().mul( sk[2+i].getImmutable().powZn(policy_set[i+1].getImmutable()) );
        }
        Element tmp2 = mG2.newElement().setToOne();
        for (int i=0; i < mNumAttributeType; i++) {
            tmp2 = tmp2.getImmutable().mul( vecH[i].getImmutable().powZn(policy_set[i].getImmutable()) );
        }
        tmp2 = tmp2.getImmutable().powZn(r.getImmutable());
        //Element result2 = tmp1.getImmutable().div(tmp2.getImmutable());
        //System.out.println("####test() - result2 = " + result2);

        Element rnd = mG2.newRandomElement();
        System.out.println("####test() - rnd = " + rnd);
        //Element result3 = vecH[0].getImmutable().div(vecH[0].getImmutable());
        Element result2 = (tmp1.getImmutable().div(tmp2.getImmutable())).mul(rnd.getImmutable());
        //result3 = result3.getImmutable().mul(rnd.getImmutable());
        System.out.println("####test() - result2 = "+result2);

        tmp1 = rnd.getImmutable();
        System.out.println("####test() - rnd = " + rnd);


        //test h_1^{x_1*y_1}...h_1^{x_n*y_n}
        for (int i=0; i < mNumAttributeType; i++) {
            tmp1 = tmp1.getImmutable().mul( (vecH[0].getImmutable().powZn(policy_set[i].getImmutable())).getImmutable().powZn(att_set[i].getImmutable()) );
        }
        System.out.println("####test() - tmp1="+tmp1);

        //gen k_i for i = 2 to n, and test K_2^{y_2}...K_n^{y_n} = (h_1^{y_1}h_2^{y_2}...h_n^{y_n})^r
        Element[] nsk = new Element[mNumAttributeType-1];

        for (int i = 0; i < mNumAttributeType-1; i++){
            //     (                            h_1^{-x_i/x_1}                                                           * h_i)^r
            nsk[i] = (  vecH[0].getImmutable().powZn( (att_set[i+1].getImmutable().negate()).div(att_set[0].getImmutable()) ).mul(vecH[i+1].getImmutable()) ).powZn(r.getImmutable());
        }

        tmp1 = mG2.newElement().setToOne();
        for (int i=0; i < mNumAttributeType-1; i++) {
            tmp1 = tmp1.getImmutable().mul( nsk[i].getImmutable().powZn(policy_set[i+1].getImmutable()) );
        }
        tmp2 = mG2.newElement().setToOne();
        for (int i=0; i < mNumAttributeType; i++) {
            tmp2 = tmp2.getImmutable().mul( vecH[i].getImmutable().powZn(policy_set[i].getImmutable()) );
        }
        tmp2 = tmp2.getImmutable().powZn(r.getImmutable());

        Element result3 = rnd.getImmutable().mul(tmp1.getImmutable().div(tmp2.getImmutable()));

        System.out.println("####test() - result3 = " + result3);
    }


    //*************************************************************************************************
    //generate the required parameters for pairing operations
    //*************************************************************************************************
    public void genPairingParameters() {
        mPairing = PairingFactory.getPairing(mCurveFilePath);
        mZr = mPairing.getZr();
        mG1 = mPairing.getG1();
        mG2 = mPairing.getG2();
        mGT = mPairing.getGT();

        //Element g1 = G1.newRandomElement();
        //Element g2 = G2.newRandomElement();
        //Element x = G1.newRandomElement();
        //Element y = G2.newRandomElement();

        //Element r1 = pairing.pairing(x,y);
        //Log.w("##x", x.toString());
        //Log.w("##y", y.toString());

        //Element e_one = G1.newElement(new BigInteger("1"));
        //Element r2 = pairing.pairing(y,x);
        //Log.w("#1", r1.toString());
        //Log.w("#2", r2.toString());
    }

    //*************************************************************************************************
    //Generate the corresponding policy set depending on the universal attribute sets and the given policy
    //*************************************************************************************************
    public Element[] genPolicySet (Element[] attribute_set) {

        return null;
    }

    //*************************************************************************************************
    //generate two vectors of element such that the inner product of two vectors is zero
    //*************************************************************************************************
    public static Element[][] genXYVec (int bit_len, int num_att) {
        Element[][] xy_vec= new Element[2][num_att]; //xy_vec[0][]: x vector, xy_vec[1][]: y vector

        Random rnd = new Random(System.currentTimeMillis());
        Element tmp = mZr.newElement();
        tmp.setToZero();

        //do {
        System.out.println("#### num_att="+ num_att);
        for (int i = 0; i < num_att-1; i++) {
            BigInteger x = new BigInteger(bit_len, rnd);
            BigInteger y = new BigInteger(bit_len, rnd);
            System.out.println("####BigInteger x = "+x);
            System.out.println("####BigInteger y = "+y);

            System.out.println("#### i = "+ i);
            xy_vec[0][i] = mZr.newElement();
            xy_vec[1][i] = mZr.newElement();
            xy_vec[0][i].set(x);
            xy_vec[1][i].set(y);

            tmp = tmp.getImmutable().add(xy_vec[0][i].getImmutable().mul(xy_vec[1][i].getImmutable()));
        }

        System.out.println("$$$");
        //} while ( tmp.mod(new BigInteger("2",10) ))!= 0 );

        System.out.println("@@@@temp:"+tmp);
        System.out.println("@@@");
        Element ntmp = tmp.getImmutable().negate();
        System.out.println("@@@@-temp:"+ntmp);
        Element t = tmp.getImmutable().add(ntmp.getImmutable());
        System.out.println("@@@@tmp-tmp=?"+t);

        xy_vec[0][num_att-1] = mZr.newElement();
        xy_vec[1][num_att-1] = mZr.newElement();
        xy_vec[0][num_att-1].set(new BigInteger("2",10));

        xy_vec[1][num_att-1]=ntmp.getImmutable().halve();


        Element t2 = tmp.getImmutable().add(xy_vec[0][num_att-1].getImmutable().mul(xy_vec[1][num_att-1].getImmutable()));
        System.out.println("@@@@tmp-2*X=?"+t2);

        System.out.println("####x["+(num_att-1)+"]="+xy_vec[0][num_att-1]);
        System.out.println("####y["+(num_att-1)+"]="+xy_vec[1][num_att-1]);

        //xy_vec[1][num_att-1] = xy_vec[1][num_att-1].getImmutable().invert();

        for (int i = 0; i < num_att; i++) {
            System.out.println("x["+i+"]=" + xy_vec[0][i]);
            System.out.println("y["+i+"]=" + xy_vec[1][i]);
        }

        //xy_vec[1][num_att-1] = xy_vec[1][num_att-1].getImmutable().invert();

        Element tmp2 = mZr.newElement();
        tmp2.setToZero();
        for (int i = 0; i < num_att; i++) {

            tmp2 = tmp2.getImmutable().add(xy_vec[0][i].getImmutable().mul(xy_vec[1][i].getImmutable()));
        }
        System.out.println("####test total="+tmp2);



        return xy_vec;
    }

    //*************************************************************************************************
    // generate new random element
    // input type: 0 - G1 element, 1 - G2 element, 2 - GT element
    // output element
    //*************************************************************************************************
    public Element newRandomElement(int type) {
        if (type == 0) {
            return mG1.newRandomElement();
        }
        else
        if (type == 1) {
            return mG2.newRandomElement();
        }
        else
        if (type == 2) {
            return mGT.newRandomElement();
        }
        return null;
    }

    private static final String FILENAME = "C:\\xampp\\htdocs\\NETS\\PrivateKey.txt";

    /* Code to write to a file private key*/
    public static void writeToFile(String content) {
        System.out.println("writing");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME))) {
            bw.write(content);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    private static final String FILENAME_public = "C:\\xampp\\htdocs\\NETS\\PublicParameters.txt";
    /* Code to write to a file public parameters*/
    public static void writeToFile_public(String content) {
        System.out.println("writing");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME_public))) {
            bw.write(content);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    private static final String FILENAME_secret = "C:\\xampp\\htdocs\\NETS\\MasterSecretKey.txt";
    /* Code to write to a file public parameters*/
    public static void writeToFile_secret(String content) {
        System.out.println("writing");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME_secret))) {
            bw.write(content);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    private static String PC(int length){

        String curve_file_path = "curves/a.properties";

        COABE co_abe = new COABE (0,curve_file_path);

        Element[][] xy_vec = co_abe.genXYVec(160,20);

        co_abe.genPairingParameters();

       // int length = args.length;
        co_abe.setNumAttType(length);


        Element M = co_abe.newRandomElement(2);

        System.out.println("Plaintext M = "+M);
       // Element[] public_param = co_abe.setPublicParams();

        Element[] PC = co_abe.PreEnc(xy_vec[1],M);

        //Element[] C = co_abe.CoEnc(PC,xy_vec[1]);

        //Element Mp = co_abe.Dec(sk,C,xy_vec[1]);

        //this is for testing
        //co_abe.test(PC,C,sk,M,xy_vec[0],xy_vec[1]);

        //System.out.println("Decrypted Plaintext M' = "+Mp);*/
        return "abc";
    }




    //****************************************
    // main function for testing...
    //****************************************
    public static void main (String[] args) {
        int length = args.length;
        PC(length);
    }
}
