package node3D;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;

import linksinterface.Window;
import see.Environment;
import see.Packet;

public class Simulatedeye extends Lightsensitiveelement{

	//Double section of visual cone
	private Border border;
	
	private boolean drawing;
	
	private static intbag defaultSee = new intbag();
	
	{
		defaultSee.store[0] = 0.1;
		defaultSee.store[1] = 100;
		defaultSee.store[2] = 8;
		defaultSee.store[3] = 8;
		defaultSee.store[4] = 4.5;
		defaultSee.store[5] = 4.5;
	}
	
	public Simulatedeye(Environment in) {
		this(in,false);
	}
	
	public Simulatedeye(Environment in,boolean drawing) {
		this(in,defaultSee,drawing);
	}
	
	public Simulatedeye(Environment in, intbag intbag, boolean drawing) {
		super(in);
		this.border = new Border(
			intbag.store[0],intbag.store[1],intbag.store[2],intbag.store[3],intbag.store[4],intbag.store[5]
		);
		
		this.drawing = drawing;
	}
	
	
	protected boolean getdrawing() {
		return this.drawing;
	}
	
	protected Border getborder() {
		return this.border;
	}
	

 	private Packet getdata() {
		return getIn().getWorld();
	}
	
	private static byte calibration(Packet pp,ArrayList<int[]> planes) {
		return 1;
	}
	
	private static byte Occlusionfiltering(Packet pp,ArrayList<int[]> planes) {
		for(int i = 0; i < pp.planes.length; i++) {
			if(checkNormal(pp.points,pp.planes[i][0],pp.planes[i][1],pp.planes[i][2])) planes.add(pp.planes[i]);
		}
		
		return 1;
	}
	
	private static double[] pa,pb;
	private static boolean checkNormal(double[][] points,int ai,int bi,int ci) {
		for(int i = 0; i < 3;i++) {
			pa[i] = points[bi][i] - points[ai][i];
			pb[i] = points[ci][i] - points[ai][i]; 
		}
		return pa[0] * pb[1] - pa[1] * pb[0] > 0;
	}
	
	private static int[] ip;
	private static byte ret;
	private static double w,h;
	private static int x,y,ipindex;
	private static byte projection(
			int[][] colors,double[][] depths, Packet pp,ArrayList<int[]> planes,projectionbag bag
		) {
		ret &= 1;
		
		for(ipindex = 0;ipindex < planes.size();ipindex++) {
			if(ret == 0) break;
			ip = planes.get(ipindex);
			ret &= (
					bag.drawing 
						? drawplance(colors,depths,pp.uvs,pp.points,ip,bag.border,pp.textures[ip[3]]) 
						: drawedge(colors,pp.points,ip,bag.border)
					);
		}

		return ret;
	}
	
	public final static byte zero = 0x00;
	public final static int BorderLine = 0xFFFFFFFF;//RGBA
	
	private static final void linkspoint(double[] from,int[] to,double[] d,int[] i) {
		to[0] = (int) ((d[0] + from[0]) / d[2] * i[0]);
		to[1] = (int) ((d[1] - from[1]) / d[3] * i[1]);
		
	}
	
	//e:E,A increment,B increment,increment 
	private static int[] e12,e23,e31,p1,p2,p3,in;
	private static int ew12,ew23,ew31,l,r,t,b,wid;
	private static double[] po1,po2,po3,po1$,po2$,po3$,uv1,uv2,uv3,bo;
	private static double Emax,u$,v$,w$,alpha,beta,gamma,aAinc,bAinc,gAinc,aBinc,bBinc,gBinc,awinc,bwinc,gwinc;
	private static byte drawplance(
			int[][] colors,double[][] depth,double[][] uvs,double[][] points,int[] index, Border bag,int[][] texture
		) {
//		double Emax;
//		int ew12,ew23,ew31;
//		
//		int[] e12,e23,e31;
//		int[] p1,p2,p3;
//		
//		double u$,v$,w$;
//		double[] po1,po2,po3;
//		double[] po1$,po2$,po3$;
//		
//		double[] uv1,uv2,uv3;
//		
//		double alpha,beta,gamma;
//		double aAinc,bAinc,gAinc;
//		double aBinc,bBinc,gBinc;
//		double awinc,bwinc,gwinc;
		
//		int[] e12,e23,e31,p1,p2,p3,in;
//		int ew12,ew23,ew31,l,r,t,b,wid,x,y;
//		double[] po1,po2,po3,po1$,po2$,po3$,uv1,uv2,uv3,bo;
//		double Emax,u$,v$,w$,alpha,beta,gamma,aAinc,bAinc,gAinc,aBinc,bBinc,gBinc,awinc,bwinc,gwinc;
//		
//		in = new int[2];bo = new double[4];
//		m = new double[2];o = new double[2];
//		p1 = new int[2];p2 = new int[2];p3 = new int[2];
//		po1$ = new double[2];po2$ = new double[2];po3$ = new double[2];
//		e12 = new int[4];e23 = new int[4];e31 = new int[4];
		
		w = bag.l + bag.r; h = bag.t + bag.b;
		
		uv1 = uvs[index[0]];uv2 = uvs[index[1]];uv3 = uvs[index[2]];
		po1 = points[index[0]];po2 = points[index[1]];po3 = points[index[2]];
		linkspoint(po1[0],po1[1],po1[2],bag.n,po1$);
		linkspoint(po2[0],po2[1],po2[2],bag.n,po2$);
		linkspoint(po3[0],po3[1],po3[2],bag.n,po3$);
		
		bo[0] = bag.l;bo[1] = bag.t;bo[2] = w;bo[3] = h;
		in[0] = colors[0].length;in[1] = colors.length;
		linkspoint(po1$,p1,bo,in);
		linkspoint(po2$,p2,bo,in);
		linkspoint(po3$,p3,bo,in);
		
		if(p1[0] > p2[0]) {l = p2[0]<p3[0]?p2[0]:p3[0];r = p1[0]>p3[0]?p1[0]:p3[0];
		} else {l = p1[0]<p3[0]?p1[0]:p3[0];r = p2[0]>p3[0]?p2[0]:p3[0];}
		if(p1[1] > p2[1]) {b = p2[1]<p3[1]?p2[1]:p3[1];t = p1[1]>p3[1]?p1[1]:p3[1];
		} else {b = p1[1]<p3[1]?p1[1]:p3[1];t = p2[1]>p3[1]?p2[1]:p3[1];}
		
		l = l < 0 ? 0 : l;
		b = b < 0 ? 0 : b;
		t = t >= colors.length  ? colors.length - 1 : t;
		r = r >= colors[0].length ? colors[0].length - 1 : r;
		
		e12[1] = p2[1] - p1[1];e12[2] = p1[0] - p2[0];e12[3] = p2[0] * p1[1] - p1[0] * p2[1];
		e12[0] = l * e12[1] + t * e12[2] + e12[3];
		
		e23[1] = p3[1] - p2[1];e23[2] = p2[0] - p3[0];e23[3] = p3[0] * p2[1] - p2[0] * p3[1];
		e23[0] = l * e23[1] + t * e23[2] + e23[3];
		
		e31[1] = p1[1] - p3[1];e31[2] = p3[0] - p1[0];e31[3] = p1[0] * p3[1] - p3[0] * p1[1];
		e31[0] = l * e31[1] + t * e31[2] + e31[3];
		
		//E12(3)
		Emax = p3[0] * e12[1] + p3[1] * e12[2] + e12[3];
		
		alpha = e12[0] / Emax;beta = e23[0] / Emax;gamma = e31[0] / Emax;
		aAinc = e12[1] / Emax;bAinc = e23[1] / Emax;gAinc = e31[1] / Emax;
		aBinc = e12[2] / Emax;bBinc = e23[2] / Emax;gBinc = e31[2] / Emax;
		
		//回形扫描
		wid = r + 1 - l;
		ew12 = wid * e12[1];ew23 = wid * e23[1];ew31 = wid * e31[1];
		awinc = wid * aAinc;bwinc = wid * bAinc;gwinc = wid * gAinc;
		for(x = l,y = t;y >= b;) {
			for(;x <= r;) {
				if(e12[0] >= 0 && e23[0] >=0 && e31[0] >= 0) {
					//depth z = 1 / w,I = (Iw) / w
					//由于屏幕y坐标相对反转，即需要校正u,v
					w$ = uv1[2] * beta + uv2[2] * gamma + uv3[2] * alpha;
					if(w$ <= depth[y][x]) {
						u$ = (uv1[0]*uv1[2]*beta+uv2[0]*uv2[2]*gamma+uv3[0]*uv3[2]*alpha) / w$;
						v$ = 1 - (uv1[1]*uv1[2]*beta+uv2[1]*uv2[2]*gamma+uv3[1]*uv3[2]*alpha) / w$;
						
						depth[y][x] = w$;
						colors[y][x] = texture[(int) (v$ * (texture.length - 1))][(int) (u$ * (texture[0].length - 1))];
					}
				}
				
				
				
				e12[0] += e12[1];e23[0] += e23[1];e31[0] += e31[1];
				alpha += aAinc;beta += bAinc;gamma += gAinc;
				x++;
			}
			//退回横坐标
			e12[0] -= ew12;e23[0] -= ew23;e31[0] -= ew31;
			alpha -= awinc;beta -= bwinc;gamma -= gwinc;
			
			x = l;y--;
			
			e12[0] -= e12[2];e23[0] -= e23[2];e31[0] -= e31[2];
			alpha -= aBinc;beta -= bBinc;gamma -= gBinc;
		}
		return 1;
	}
	
	private static double[] poi1,poi2,poi3;
	private static byte drawedge(
			int[][] colors,double[][] points,int[] index,Border border
		) {
		poi1 = points[index[0]];
		poi2 = points[index[1]];
		poi3 = points[index[2]];
		
		drawline(colors,poi1,poi2,BorderLine,border);
		drawline(colors,poi1,poi3,BorderLine,border);
		drawline(colors,poi2,poi3,BorderLine,border);

		return 1;
	}
	
	private static double Mp;
	private static final void linkspoint(double px,double py,double pz,double nearplaneZ,double[] retpoint) {
		//忽略正负性，不考虑任何可视平面大于0的任何情况即使它需要考虑
		//当点深度大于0，它将是个虚像，因为忽略正负性，被选择像对于xy平面是对称的，该方法将会错误的不选择虚像，该方法不承担任何后果
		Mp = Math.abs(nearplaneZ / (pz));
		
		retpoint[0] = px * Mp;
		retpoint[1] = py * Mp;
	}
	
	//透视变换的线
	private static int i;
	private static double d,vd,p,V,H;
	private static double winp,hinp,pixelx,pixely,pixelz,nearplaneZ,farplaneZ,haspixel;
	private static double[] main,other,m,o;
	private static final void drawline(int[][] colors,double[] p1,double[] p2,int color,Border bag) {
//		int i,x,y;
//		double w,h,d,vd,p,V,H;
//		double winp,hinp,pixelx,pixely,pixelz,nearplaneZ,farplaneZ,haspixel;
//		double[] main,other,m = new double[2],o = new double[2];
		
		nearplaneZ = 0 - bag.n;farplaneZ = 0 - bag.f;
		if(p1[2] > nearplaneZ) {
			if(p2[2] > nearplaneZ) return;//该线段必定不显示
			main = p2; other = p1;
		} else { main = p1; other = p2;}
		
		w = main[0] - other[0];h = main[1] - other[1];d = main[2] - other[2];//宽、高、深
		vd = main[2] - nearplaneZ;
		p = vd > d ? (d / vd) : 1;
		
		linkspoint(main[0],main[1],main[2],nearplaneZ,m);
		linkspoint(main[0] - p * w,main[1] - p * h,main[2] - p * d,nearplaneZ,o);
		
		winp = Math.abs(m[0] - o[0]); //width in near plane
		hinp = Math.abs(m[1] - o[1]); //height in near plane
		
		o[0] = m[0] - o[0];
		o[1] = m[1] - o[1];

		haspixel = Math.max(
			 Math.ceil((winp / (bag.l + bag.r)) * colors[0].length), 
			 Math.ceil((hinp / (bag.n + bag.b)) * colors.length));

		for(i = 0; i <= haspixel;i++) {//线段大概率不连续，即使可用x、y像素量取大来解决，但保留问题
			pixelz = main[2] - d * (p = i / haspixel);
			if(pixelz < farplaneZ || nearplaneZ < pixelz) continue;//不在可见深度
			
			pixelx = m[0] - o[0] * p;
			V = pixelx < 0 ? bag.l : bag.r;
			if(Math.abs(pixelx) > V) continue;
			
			pixely = m[1] - o[1] * p;
			H = pixely < 0 ? bag.b : bag.t;
			if(Math.abs(pixely) > H) continue;
			
			x = (int) ((bag.l + pixelx) / (bag.l + bag.r) * colors[0].length);
			y = (int) ((bag.t - pixely) / (bag.t + bag.b) * colors.length);
			
			colors[y][x] = color;
		}
		
	}
		
	@SuppressWarnings("unused")
	private static byte flush(int[][][] colors,Simulateretina window) {
		byte ret = 1;
		
		try {
			window.flush(colors);
		} catch(Exception e) {ret &= 0;}
		
		return ret;
	}
	
	private static byte init(){
		try {
			//Everything
			ret = 1;
			//Occlusionfiltering
			pa = new double[3];pb = new double[3];
			//projection
			in = new int[2];bo = new double[4];
			m = new double[2];o = new double[2];
			p1 = new int[2];p2 = new int[2];p3 = new int[2];
			po1$ = new double[2];po2$ = new double[2];po3$ = new double[2];
			e12 = new int[4];e23 = new int[4];e31 = new int[4];
			
		
		}catch(Exception e) {return 0;}
		
		return 1;
	}
	
	//index key in color is x.y.[r,g,b]
	public void run(int width,int height) throws Exception {
		Packet Pp;
		projectionbag bag;
		ArrayList<int[]> planes = new ArrayList<>();
		
		double[][] depth;
		int[][] colors;
		
//		Simulateretina window = new Simulateretina(width,height);
//		window.open();
		
		testWindow window = new testWindow(width,height);
		int w = window.getwidth();int h = window.getheight();
		
		colors = new int[h][w];
		bag = new projectionbag(null,false);
		depth = drawing ? new double[h][w] : null;
		
		double initdepth;int has = 0,l,r;
		long start = System.currentTimeMillis();
		for(byte flag = init();System.currentTimeMillis() - start <= 20 * 1000;) {
			if(flag == 0) throw new runable(this.mode);
			
			planes.clear();
			Pp = getdata();
			
			for(l = 0;l<w;l++)for(r = 0;r<h;r++) colors[r][l] = 0xFFFFFFFF;
			if(drawing) {
				initdepth = 1 / Double.MIN_VALUE;bag.drawing = true;
				for(l = 0;l<w;l++)for(r = 0;r<h;r++) depth[r][l] = initdepth;
			}
			bag.border = this.border;
			
			if(flag > 0) flag &= calibration(Pp, planes);
			if(flag > 0) flag &= Occlusionfiltering(Pp,planes);
			if(flag > 0) flag &= projection(colors, depth,Pp, planes,bag);
//			if(flag > 0) flag &= flush(colors,window);
			
			window.flush(colors);
			has++;
		}
		
//		window.close();
		window.dead();
		
		System.out.println(has / 20);
	}
	
	protected runable makeError() {
		return new runable(this.mode);
	}
	
	private class runable extends Exception {
		private static final long serialVersionUID = 1L;
		
		public runable(Mode m) { super(m.getMessage());}
	}
	
	public static class intbag 
	{ 
		//new int[]{near,far,leftmright,top,bottom}
		public final double[] store = new double[6];
	}
	
	protected static class projectionbag 
	{
		public Border border;
		public boolean drawing;
		
		protected projectionbag(Border border, boolean drawing) {
			this.border = border;
			this.drawing = drawing;
		}
		
	}
	
	public final static class Border{
		public double n,f,l,r,t,b;
		byte[][][][] textures;
		
		public Border(double n,double f,double l,double r,double t,double b) {
			this.n = n;
			this.f = f;
			this.l = l;
			this.r = r;
			this.t = t;
			this.b = b;
		}
	}
	
	public static class testWindow
	{
		private int width,height;
		private BufferedImage image;
		private JFrame frame = new JFrame("test") {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void paint(Graphics g) {
				g.drawImage(image,0,0,null);
			}
			
		};
		
		public testWindow(int width, int height) {
			frame.setVisible(true);
			frame.setSize(width, height);
			frame.setLocationRelativeTo(null);
			this.width = frame.getWidth();this.height = frame.getHeight();
			image = new BufferedImage(this.width,this.height,BufferedImage.TYPE_INT_RGB);
		}
		
		
		public void dead() {
			frame.dispose();
		}
		
		public void repaint() {
			frame.repaint();
		}
		
		public void flush(int[][] colors) {
			int w = colors[0].length;
			for(int h = 0; h < colors.length; h++) {
				image.setRGB(0, h, width, 1,colors[h], 0, w);
			}
			repaint();
		}
		
		public void flush(int[] colors,int w) {
			image.setRGB(0, 0, width, height,colors, 0, w);
			repaint();
		}
		
		public int getwidth() { return width;}
		public int getheight() { return height;}
		
	}
	
	public class Simulateretina extends Window
	{
		
		public Simulateretina(int width,int height) {
			super(width,height);
		}
		
		public void open() {
			super.Selfpresentation();
		}
		
		public void close() {
			super.suicide();
		}
		
		public void minimization() {
			super.Selfhiding();
		}

		public void flush(int[][][] colors) {
			System.out.println("in");
			super.flushcanvas(super.myhanel,colors);
		}

	}
	
	private Simulateretina window;

	@SuppressWarnings("unused")
	private Simulateretina getWindow() {
		return window;
	}

	@SuppressWarnings("unused")
	private void setWindow(Simulateretina window) {
		this.window = window;
	}
	
	private Mode mode = Mode.giveup;
	protected void setmode(Mode mode) {
		this.mode = mode;
	}
	protected Mode getmode() {
		return this.mode;
	}
	
}
