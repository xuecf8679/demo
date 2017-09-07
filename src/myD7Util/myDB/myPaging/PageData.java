package myD7Util.myDB.myPaging;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.variant.Record;


public class PageData extends Page<Record>{
	private PageData(int pageSize,int pageNo){
		super(pageSize, pageNo);
	}
	public static PageData getInstance(int pageSize,int pageNo){
		return new PageData(pageSize,pageNo);
	}
	public static PageData getInstance(Page page){
		PageData pd=new PageData(page.getPageSize(),page.getPageNo());
		pd.setEntities(page.getEntities());
		pd.setEntityCount(page.getEntityCount());
		return pd;
	}
	
	//
	public List<Record> getRecordList(){
		Collection<Record> c=super.getEntities();
		List<Record> returnList=new ArrayList<Record>();
		returnList.addAll(c);
		return returnList;
	}
	public int getTotalRecordCount(){
		return super.getEntityCount();
	}
	public int getFirstRecordIndex(){
		return super.getFirstEntityIndex();
	}
	public int getLastRecordIndex(){
		return super.getLastEntityIndex();
	}
	
	public void setRecordList(Collection<Record> recordList){
		super.setEntities(recordList);
	}
	
	public void setTotalRecordCount(int totalRecordCount){
		super.setEntityCount(totalRecordCount);
	}
	//
	public Page toD7Page(){
		return (Page)this;
	}
	
}
