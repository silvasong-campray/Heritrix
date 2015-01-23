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
	   String hql="from NapProduct as nap where nap.pid=:pid";
	   Query query=s.createQuery(hql);
	   query.setString("pid", pid);
	   int count = query.list().size();
	   s.close();
	   return count;
       
   }
    
public static void getProductInfo(String html,String url) throws IOException, JSONException {
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
	//
	Document productInfo = Jsoup.parse(html);
	if(url.contains("http://www.suning.com/emall/cprd_")){
		vendor=url.split("_")[3];
	
		element = productInfo.getElementById("productInfoUl");
		 partNumber="000000000"+element.getElementsByTag("li").get(0).text().replaceAll("商品编码", "").trim();
		//name
        Elements elements = productInfo.getElementsByClass("product-main-title");
		productName=elements.text();
		//vendor
		element=productInfo.getElementById("curShopName");
		if(element!=null){
		shopName=element.text();
		}
		//params
		element=productInfo.getElementById("canshu_box");
		if (element != null) {
            productParam=element.text().replaceAll("纠错", " ");
		}
		//category
		element = productInfo.getElementById("crumbs");
		String str[]=element.text().split(">");
		productCatagory=str[1]+">"+str[2]+">"+str[3];
				
		//images
		element=productInfo.getElementById("preView_box");  
		for(int i=0;i<element.select("img").size();i++){
		imageLink+=element.select("img").get(i).attr("src")+"#";
		}
		
		}else if(Pattern.compile("http\\://product\\.suning\\.com/\\d{10}/\\d{9}.html.*").matcher(url).find()){
		index=0;
		for(int i=0;i<3;i++)
		{
			index=url.indexOf("/", index);
			index++;
		}
		vendor=url.substring(index, index+10);
		partNumber="000000000"+url.substring(index+11,index+20);
		Document Vender = Jsoup.connect("http://product.suning.com/emall/csl_10052_10051_00000000_"+partNumber+"_9051_.html").timeout(10000).get();
		if(Vender.text().startsWith("{")){
			jsonObject=new JSONObject(Vender.text());
			shopName=jsonObject.getJSONArray("shopList").getJSONObject(0).getString("shopName");
		}
		//产品名字
		Elements elements = productInfo.getElementsByClass("proinfo-title");
		productName=elements.get(0).child(0).text();
				
		//产品参数
		element=productInfo.getElementById("itemParameter");
		if (element != null) {
			productParam=element.text().replaceAll("纠错", " ");
		}
				
		//产品分类
		elements = productInfo.getElementsByClass("breadcrumb");
		productCatagory=elements.get(0).children().get(2).text()+">"+elements.get(0).children().get(4).text()+">"+elements.get(0).children().get(6).children().get(0).text();
				
		//产品图片
		elements=productInfo.getElementsByClass("imgzoom-thumb-main");  
		for(int i=0;i<elements.select("img").size();i++){
			imageLink += elements.select("img").get(i).attr("src-medium")+"#";
		}
		
	}else{
		index=0;
		for(int i=0;i<3;i++)
		{
			index=url.indexOf("/", index);
			index++;
		}
		partNumber="000000000"+url.substring(index,index+9);
		Document Vender = Jsoup.connect("http://product.suning.com/emall/csl_10052_10051_00000000_"+partNumber+"_9051_.html").timeout(10000).get();
		if(Vender.text().startsWith("{")){
			jsonObject=new JSONObject(Vender.text());
			vendor=jsonObject.getJSONArray("shopList").getJSONObject(0).getString("shopCode");
			if(vendor.equals("SN_001")){
				vendor="0000000000";
			}
			shopName=jsonObject.getJSONArray("shopList").getJSONObject(0).getString("shopName");
		}
		//产品名称
		Elements elements = productInfo.getElementsByClass("proinfo-title");
		productName=elements.get(0).child(0).text();
		
		
		//产品参数
		element=productInfo.getElementById("itemParameter");
		if (element != null) {
			 productParam=element.text().replaceAll("纠错", " ");
		}else{
			return;
		}
		
		//产品分类
		 elements = productInfo.getElementsByClass("breadcrumb");
		 productCatagory=elements.get(0).children().get(2).text()+">"+elements.get(0).children().get(4).text()+">"+elements.get(0).children().get(6).children().get(0).text();
		
		  //产品图片
		elements=productInfo.getElementsByClass("imgzoom-thumb-main");  
		for(int i=0;i<elements.select("img").size();i++){
			imageLink += elements.select("img").get(i).attr("src-medium")+"#";
		}
		
	}
	if(productCatagory.equals(">>邮费")){
		return;
	}
	if(productParam.equals("")){
		return;
	}
	
	
	// 产品价格
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
	    
	    netPrice=jsonObject.getString("refPrice");
	    if(netPrice.isEmpty()||netPrice.equals("")){
	    	netPrice=jsonObject.getString("netPrice");
	    }
	    
	}
	else{
		return;
	}
	
	SuProduct product=new SuProduct();
	product.setProductCatagory(productCatagory);
	product.setPartNumber(partNumber);
	product.setStoreId(storeId);
	product.setCatalogId(catalogId);
	product.setProductLink(url);
	product.setPromotionPrice(Float.parseFloat(promotionPrice));
	product.setNetPrice(Float.parseFloat(netPrice));
	product.setProductImage(imageLink);
	product.setVendor(vendor);
	product.setVendorName(shopName);
	product.setProductName(productName);
	product.setProductParam(productParam);
	product.setCreatetime(System.currentTimeMillis());
	product.setDisPrice(Float.parseFloat(promotionPrice)-Float.parseFloat(netPrice));
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
		
		NapProduct nPro = new NapProduct();
		
		nPro.setBrand(brand);
		nPro.setPid(pid);
		nPro.setName(name);
		nPro.setPrice(Float.parseFloat(price.replace("USD", "")));
		nPro.setImage(image);
		nPro.setDescription(description);
		nPro.setUrl(url);
		nPro.setCategory(category);
		nPro.setCreatetime(System.currentTimeMillis());
		dataPersistence(nPro);
}


}
