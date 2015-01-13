package org.com.product;
import java.io.File;
import java.io.IOException;
import java.net.HttpCookie;
import java.util.Random;
import java.util.regex.Pattern;

import net.sf.json.JSON;

import org.archive.io.RecordingInputStream;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class  HtmlAnalysis{

	private static String cityId = "9051";
	private static String clientType = "1";
    private static Session s;
    
	private static SessionFactory sf=HibernateBase.getSessionFactory();
   public static void dataPersistence(Object o){
	    
		s=sf.openSession();
    	Transaction tx=s.beginTransaction();
		s.save(o);
        tx.commit();
		sf.close();
		
		
    }
    
    public static int findByPid(String pid)
   {
	   s=sf.openSession();
	   String hql="from napProduct as nap where nap.pid=:pid";//使用命名参数，推荐使用，易读。
	   Query query=s.createQuery(hql);
	   query.setString("pid", pid);
	   int count = query.list().size();
	   s.close();
	   return count;
       
   }
    /*
	public static void getProductInfo(String uri) throws JSONException, IOException {
		// TODO Auto-generated method stub
		JSONObject price,category;
		Elements elements;
		String p1;
		Product product=new Product();
		String param = "";
		String vendor = "0000000000";
		String storeId="";
		String catalogId="";
		String partNumber="";
		String imageLink="";
		
		
		Document doc= Jsoup.connect(uri).timeout(10000).get();
		
		
		
		// 提取产品价格
		elements = doc.getElementsByTag("head");
		String script = elements.toString();
		int a = script.indexOf("storeId");
		int b = script.indexOf("catalogId");
		int c = script.indexOf("partNumber");
		int d = script.indexOf("vendorCode");

		storeId = script.substring(a + 10, a + 15);
		catalogId = script.substring(b + 12, b + 17);
		partNumber = script.substring(c + 13, c + 31);
		
		if (script.substring(d + 14, d + 16).equals("00")) {
			vendor=script.substring(d + 14, d + 24);
			param = "http://product.suning.com/SNProductStatusView?"
					+ "storeId=" + storeId + "&catalogId=" + catalogId
					+ "&partNumber=" + partNumber + "&vendorCode=" + vendor
					+ "&cityId=9051&clientType=1";
		} else {
			param = "http://product.suning.com/SNProductStatusView?"
					+ "storeId=" + storeId + "&catalogId=" + catalogId
					+ "&partNumber=" + partNumber + "&vendor=" + vendor
					+ "&cityId=9051&clientType=1";
		}
		
		
		

		
		elements = doc.getElementsByClass("product-main-title");
		product.setProductName(elements.text());
		
		org.jsoup.nodes.Element productInfo= doc.getElementById("canshu_box");
		category=new JSONObject(script.substring(script.indexOf("{",script.indexOf("snqa")),script.indexOf("}",script.indexOf("snqa"))+1 ));
		
		product.setProductCatagory(category.getString("categoryName1")+"/"+category.getString("categoryName2")+"/"+category.getString("categoryName3"));
		
		//提取商家信息
		org.jsoup.nodes.Element vendorInfo=doc.getElementById("curShopName");
		if(vendorInfo!=null){
			product.setVendorName(vendorInfo.text());
		}
		else{
			product.setVendorName("苏宁自营");
		}
		// 提取产品参数
		if (productInfo != null) {
			
			product.setProductInfo(productInfo.text().replaceAll("纠错", " "));
		}
		else{
			return;
		}
		// 提取产品价格
				Document doc1 = Jsoup.connect(param).timeout(10000).get();
				if((p1=doc1.text()).startsWith("{")){
				price = new JSONObject(p1);
				if(price.getString("promotionPrice").equals("")&&price.getString("netPrice").equals("")){
					return;
				}
				product.setPromotionPrice(price.getString("promotionPrice"));
			    product.setNetPrice(price.getString("netPrice"));
				}
				else{
					return;
				}
		
		//提取产品图片链接
		org.jsoup.nodes.Element productImage= doc.getElementById("preView_box");
		for(int i=0;i<productImage.select("img").size();i++){
		imageLink+=productImage.select("img").get(i).attr("src")+"#";
		};
		product.setProductLink(uri);
		product.setProductImage(imageLink);
		product.setStoreId(storeId);
		product.setCatalogId(catalogId);
		product.setPartNumber(partNumber);
		product.setVendor(vendor);
		//持久化数据
		dataPersistence(product);
		 
		
	}*/
public static void getProductInfo(String uri) throws IOException, JSONException {
	// TODO Auto-generated method stub
	JSONObject jsonObject;
	Element element;
	int index;
	String storeId="10052";
	String catalogId="10051";
	String partNumber="";
	String productName="";
	String vendor="";
	String shopName="";
	String productParam="";
	String imageLink="";
	String promotionPrice="";
	String netPrice="";
	String productCatagory="";
	if(uri.contains("http://www.suning.com/emall/cprd_")){
		vendor=uri.split("_")[3];
		}else if(Pattern.compile("http\\://product\\.suning\\.com/\\d{10}/\\d{9}.html.*").matcher(uri).find()){
		index=0;
		for(int i=0;i<3;i++)
		{
			index=uri.indexOf("/", index);
			index++;
		}
		vendor=uri.substring(index, index+10);
		//partNumber="000000000"+uri.substring(index+11,index+20);
	}else{
		index=0;
		for(int i=0;i<3;i++)
		{
			index=uri.indexOf("/", index);
			index++;
		}
		partNumber="000000000"+uri.substring(index,index+9);
		Document Vender = Jsoup.connect("http://product.suning.com/emall/csl_10052_10051_00000000_"+partNumber+"_9051_.html").timeout(10000).get();
		if(Vender.text().startsWith("{")){
			jsonObject=new JSONObject(Vender.text());
			vendor=jsonObject.getJSONArray("shopList").getJSONObject(0).getString("shopCode");
			if(vendor.equals("SN_001")){
				vendor="0000000000";
			}
			shopName=jsonObject.getJSONArray("shopList").getJSONObject(0).getString("shopName");
		}
	}
	
	//获取产品基础信息
	Document productInfo = Jsoup.connect(uri).timeout(10000).get();
	   //产品编号
	element = productInfo.getElementById("productInfoUl");
	partNumber="000000000"+element.getElementsByTag("li").get(0).text().replaceAll("商品编码", "").trim();
	   //产品名称
	Elements elements = productInfo.getElementsByClass("product-main-title");
	productName=elements.text();
	   //产品店铺名称
	element=productInfo.getElementById("curShopName");
	if(element!=null){
		shopName=element.text();
	}
    //产品参数
	element=productInfo.getElementById("canshu_box");
	if (element != null) {
		productParam=element.text().replaceAll("纠错", " ");
	}
	else{
		return;
	} 
	   //产品类别
	element = productInfo.getElementById("crumbs");
	productCatagory=element.text().split(">")[3];
	  //产品图片
	element=productInfo.getElementById("preView_box");  
	for(int i=0;i<element.select("img").size();i++){
		imageLink+=element.select("img").get(i).attr("src")+"#";
		}
	  
	// 提取产品价格
	String param;
	if (vendor.equals("0000000000")) {
		param = "http://www.suning.com/emall/SNProductStatusView?"
				+ "storeId=" + storeId + "&catalogId=" + catalogId
				+ "&partNumber=" + partNumber + "&vendor=0000000000"
				+ "&cityId=9051&clientType=1";
		
	} else {
		
		param = "http://www.suning.com/emall/SNProductStatusView?"
				+ "storeId=" + storeId + "&catalogId=" + catalogId
				+ "&partNumber=" + partNumber + "&vendorCode=" + vendor
				+ "&cityId=9051&clientType=1";
	}
	Document  Price= Jsoup.connect(param).timeout(10000).get();
	if(Price.text().startsWith("{")){
		jsonObject = new JSONObject(Price.text());
	    if(jsonObject.getString("promotionPrice").equals("")&&jsonObject.getString("netPrice").equals("")){
		  return;
	    }
	    promotionPrice = jsonObject.getString("promotionPrice");
	    netPrice=jsonObject.getString("netPrice");
	    
	}
	else{
		return;
	}
	
	Product product=new Product();
	product.setProductCatagory(productCatagory);
	product.setPartNumber(partNumber);
	product.setStoreId(storeId);
	product.setCatalogId(catalogId);
	product.setProductLink(uri);
	product.setPromotionPrice(promotionPrice);
	product.setNetPrice(netPrice);
	product.setProductImage(imageLink);
	product.setVendor(vendor);
	product.setVendorName(shopName);
	product.setProductName(productName);
	product.setProductParam(productParam);
	dataPersistence(product);
	
}

public static void getNapProduct(String html,String url) throws IOException{
	
     String pid;
	
	 String name;
	
	 String price;
	
	 String brand;
	
	 String category;
	
	 String image="";
	
	 String description;
	
	 Element element;
     
	   // Document page = Jsoup.connect(Url).timeout(10000).get();
	    Document page =Jsoup.parse(html);
	    element = page.getElementById("product-details");
		pid=element.attr("data-pid");
		name=element.attr("data-name");
		price=element.attr("data-price");
		brand=element.attr("data-brand");
		category=element.attr("data-category");
	    element = page.getElementById("editors-notes-content");
	    description = element.text();
		element = page.getElementById("thumbnails-container");
		Elements images = element.getElementsByTag("meta");
		for(int i=0 ; i<images.size();i++){
			image+=images.get(i).attr("content")+"#";
		}
		
		napProduct nPro = new napProduct();
		
		nPro.setBrand(brand);
		nPro.setPid(pid);
		nPro.setName(name);
		nPro.setPrice(price);
		nPro.setImage(image);
		nPro.setDescription(description);
		nPro.setUrl(url);
		nPro.setCategory(category);
		dataPersistence(nPro);
}


}
