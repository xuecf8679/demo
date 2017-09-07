package myD7Util.myDB;

import java.util.*;

import myD7Util.AssertUtil;
import myD7Util.myTrans.DateTransUtil;
/**
 *
 * @author xuecf
 */
public class SqlStatement {
    //
    private String tableName="";
    private ArrayList<String> insertOrUpdateKeyNames=new ArrayList<String>();
    private ArrayList insertOrUpdateKeyValues=new ArrayList();
    private HashMap<String,Object> insertOrUpdateKeyNamesAndValues=new HashMap<String,Object>();
    private HashMap<String,Object> whereKeyNamesAndValues=new HashMap<String,Object>();
    private ArrayList<String> whereJoinStrings=new ArrayList<String>();
    private String instanceFor="";//查看当前实例是干什么用的:delete/update/insert/oracleDelete/oracleUpdate/oracleInsert
    public String sqlString;//封装该实例所对应的sql语句
    //
    private SqlStatement(){
    	super();
    }
    public String toString(){
    	return this.getSqlString();
    }
    //
    public static void main(String[] args){
        HashMap<String,Object> hm1=new HashMap<String,Object>();
        hm1.put("keyname1","keyvalue1");
        hm1.put("keyname2",null);
        hm1.put("keyname3",new Date());
        hm1.put("keyname4","hehe");
        hm1.put("keyname5","");
        HashMap<String,Object> hm2=new HashMap<String,Object>();
        hm2.put("wherekeyname1",null);
        hm2.put("wherekeyname2",3);
        ArrayList<String> al1=new ArrayList<String>();
        al1.add("or");
        System.out.println(SqlStatement.getInstanceForInsert("tablename1",hm1).getSqlString());
        System.out.println(SqlStatement.getInstanceForUpdate("tablename1",hm1,hm2,al1).getSqlString());
        System.out.println(SqlStatement.getInstanceForDelete("tablename1",hm2).getSqlString());
        //
        System.out.println(SqlStatement.getInstanceForOracleInsert("tablename1",hm1).getSqlString());
        System.out.println(SqlStatement.getInstanceForOracleUpdate("tablename1",hm1,hm2,al1).getSqlString());
        System.out.println(SqlStatement.getInstanceForOracleDelete("tablename1",hm2).getSqlString());
    }
    public static SqlStatement getInstanceForUpdate(String tableName,HashMap<String,Object> insertOrUpdateKeyNamesAndValues,String whereKeyName,Object whereKeyValue){
        SqlStatement dbp=new SqlStatement();
        dbp.setTableName(tableName);
        dbp.fromMapToArrayList(insertOrUpdateKeyNamesAndValues);
        HashMap<String,Object> hm=new HashMap<String,Object>();
        hm.put(whereKeyName, whereKeyValue);
        dbp.setWhereKeyNamesAndValues(hm);
        dbp.setInstanceFor("update");
        dbp.setSqlString(dbp.getUpdateStatement());
        return dbp;
    }
    
    public static SqlStatement getInstanceForUpdate(String tableName,HashMap<String,Object> insertOrUpdateKeyNamesAndValues,HashMap<String,Object> whereKeyNamesAndValues){
        SqlStatement dbp=new SqlStatement();
        dbp.setTableName(tableName);
        dbp.fromMapToArrayList(insertOrUpdateKeyNamesAndValues);
        dbp.setWhereKeyNamesAndValues(whereKeyNamesAndValues);
        dbp.setInstanceFor("update");
        dbp.setSqlString(dbp.getUpdateStatement());
        return dbp;
    }
    public static SqlStatement getInstanceForUpdate(String tableName,HashMap<String,Object> insertOrUpdateKeyNamesAndValues,HashMap<String,Object> whereKeyNamesAndValues,ArrayList<String> whereJoinStrings){
        SqlStatement dbp=new SqlStatement();
        dbp.setTableName(tableName);
        dbp.fromMapToArrayList(insertOrUpdateKeyNamesAndValues);
        dbp.setWhereKeyNamesAndValues(whereKeyNamesAndValues);
        dbp.setWhereJoinStrings(whereJoinStrings);
        dbp.setInstanceFor("update");
        dbp.setSqlString(dbp.getUpdateStatement());
        return dbp;
    }
    public static SqlStatement getInstanceForInsert(String tableName,HashMap<String,Object> insertOrUpdateKeyNamesAndValues){
        SqlStatement dbp=new SqlStatement();
        dbp.setTableName(tableName);
        dbp.fromMapToArrayList(insertOrUpdateKeyNamesAndValues);
        dbp.setInstanceFor("insert");
        dbp.setSqlString(dbp.getInsertStatement());
        return dbp;
    }
    
    
    public static SqlStatement getInstanceForDelete(String tableName,String whereKeyName,Object whereKeyValue){
        SqlStatement dbp=new SqlStatement();
        dbp.setTableName(tableName);
        HashMap<String,Object> hm=new HashMap<String,Object>();
        hm.put(whereKeyName, whereKeyValue);
        dbp.setWhereKeyNamesAndValues(hm);
        dbp.setInstanceFor("delete");
        dbp.setSqlString(dbp.getDeleteStatement());
        return dbp;
    }
    public static SqlStatement getInstanceForDelete(String tableName,HashMap<String,Object> whereKeyNamesAndValues){
        SqlStatement dbp=new SqlStatement();
        dbp.setTableName(tableName);
        dbp.setWhereKeyNamesAndValues(whereKeyNamesAndValues);
        dbp.setInstanceFor("delete");
        dbp.setSqlString(dbp.getDeleteStatement());
        return dbp;
    }
    public static SqlStatement getInstanceForDelete(String tableName,HashMap<String,Object> whereKeyNamesAndValues,ArrayList<String> whereJoinStrings){
        SqlStatement dbp=new SqlStatement();
        dbp.setTableName(tableName);
        dbp.setWhereKeyNamesAndValues(whereKeyNamesAndValues);
        dbp.setWhereJoinStrings(whereJoinStrings);
        dbp.setInstanceFor("delete");
        dbp.setSqlString(dbp.getDeleteStatement());
        return dbp;
    }
    
    private void fromMapToArrayList(HashMap<String,Object> insertOrUpdateKeyNamesAndValues){
    	AssertUtil.Assert(!insertOrUpdateKeyNamesAndValues.isEmpty());
        Set<String> keyNames=insertOrUpdateKeyNamesAndValues.keySet();
        Iterator<String> it=keyNames.iterator();
        while(it.hasNext()){
            String keyName=it.next();
            insertOrUpdateKeyNames.add(keyName);
            insertOrUpdateKeyValues.add(insertOrUpdateKeyNamesAndValues.get(keyName));
        }
    }
    private String getInsertStatement(){
    	AssertUtil.Assert(insertOrUpdateKeyNames.size()>0&&insertOrUpdateKeyNames.size()==insertOrUpdateKeyValues.size());
    	AssertUtil.Assert(!tableName.equals(""));
        String insertSql="insert into "+tableName+"(";
        String fieldsSql="";
        for(int n=0;n<insertOrUpdateKeyNames.size();n++){
            String keyName=insertOrUpdateKeyNames.get(n);
            if(n==insertOrUpdateKeyNames.size()-1){
                fieldsSql=fieldsSql+keyName;
            }else{
                fieldsSql=fieldsSql+keyName+",";
            }
        }
        insertSql=insertSql+fieldsSql+") values(";
        String valuesSql="";
        for(int v=0;v<insertOrUpdateKeyValues.size();v++){
            String keyValue="";
            Object value=insertOrUpdateKeyValues.get(v);
            if(value==null){
                keyValue="null";
                if(v==insertOrUpdateKeyValues.size()-1){
                    valuesSql=valuesSql+keyValue;
                }else{
                    valuesSql=valuesSql+keyValue+",";
                }
            }else if(value instanceof Number){
                keyValue=value.toString();
                if(v==insertOrUpdateKeyValues.size()-1){
                    valuesSql=valuesSql+keyValue;
                }else{
                    valuesSql=valuesSql+keyValue+",";
                }
            }else if(value instanceof Date){
                keyValue=DateTransUtil.getDateString$yyyy_MM_dd_HH_mm_ss_SSS((Date)value);
                if(v==insertOrUpdateKeyValues.size()-1){
                    valuesSql=valuesSql+"'"+keyValue+"'";
                }else{
                    valuesSql=valuesSql+"'"+keyValue+"',";
                }
            }else{
                keyValue=value.toString();
                if(v==insertOrUpdateKeyValues.size()-1){
                    valuesSql=valuesSql+"'"+keyValue+"'";
                }else{
                    valuesSql=valuesSql+"'"+keyValue+"',";
                }
            }       
        }
        insertSql=insertSql+valuesSql+")"; 
        return insertSql;
    }
    private String getUpdateStatement(){
    	AssertUtil.Assert(insertOrUpdateKeyNames.size()>0&&insertOrUpdateKeyNames.size()==insertOrUpdateKeyValues.size());
    	AssertUtil.Assert(!tableName.equals(""));
        String updateSql="update "+tableName+" set ";
        String setSql="";
        for(int s=0;s<insertOrUpdateKeyNames.size();s++){
            String keyName=insertOrUpdateKeyNames.get(s);
            String keyValue="";
            Object value=insertOrUpdateKeyValues.get(s);
            if(value==null){
                keyValue="null";
                if(s==insertOrUpdateKeyNames.size()-1){
                    setSql=setSql+keyName+"="+keyValue;
                }else{
                    setSql=setSql+keyName+"="+keyValue+",";
                }
            }else if(value instanceof Number){
                keyValue=value.toString();
                if(s==insertOrUpdateKeyNames.size()-1){
                    setSql=setSql+keyName+"="+keyValue;
                }else{
                    setSql=setSql+keyName+"="+keyValue+",";
                }
            }else if(value instanceof Date){
                keyValue=DateTransUtil.getDateString$yyyy_MM_dd_HH_mm_ss_SSS((Date)value);
                if(s==insertOrUpdateKeyNames.size()-1){
                    setSql=setSql+keyName+"='"+keyValue+"'";
                }else{
                    setSql=setSql+keyName+"='"+keyValue+"',";
                }
            }else{
                keyValue=value.toString();
                if(s==insertOrUpdateKeyNames.size()-1){
                    setSql=setSql+keyName+"='"+keyValue+"'";
                }else{
                    setSql=setSql+keyName+"='"+keyValue+"',";
                }
            }        
        }
        updateSql=updateSql+setSql+" where ";
        int mapSize=whereKeyNamesAndValues.size();
        assert(mapSize>=1);
        if(mapSize==1){
            Set<String> whereKeyNameSet=whereKeyNamesAndValues.keySet();
            Iterator<String> it=whereKeyNameSet.iterator();
            String whereKeyName=it.next();
            Object whereKeyValue=whereKeyNamesAndValues.get(whereKeyName);
            if(whereKeyValue==null){
                updateSql=updateSql+whereKeyName+" is null";
            }else{
                if(whereKeyValue instanceof Number){
                    updateSql=updateSql+whereKeyName+"="+whereKeyValue.toString();
                }else if(whereKeyValue instanceof Date){
                    String dateValue=DateTransUtil.getDateString$yyyy_MM_dd_HH_mm_ss_SSS((Date)whereKeyValue);
                    updateSql=updateSql+whereKeyName+"='"+dateValue+"'";
                }else{
                    updateSql=updateSql+whereKeyName+"='"+whereKeyValue.toString()+"'";
                }
            }
            
        }else{
            int listSize=whereJoinStrings.size();
            if(listSize==0){
                for(int a=1;a<mapSize;a++){
                    whereJoinStrings.add("and");
                }
            }
            listSize=whereJoinStrings.size();
            AssertUtil.Assert(listSize==mapSize-1);
            Set<String> whereKeyNameSet=whereKeyNamesAndValues.keySet();
            Object[] whereKeyNameArray=whereKeyNameSet.toArray();
            for(int a=0;a<whereKeyNameArray.length;a++){
                String whereKeyName=(String)whereKeyNameArray[a];
                Object whereKeyValue=whereKeyNamesAndValues.get(whereKeyName);
                if(whereKeyValue==null){
                    if(a==whereKeyNameArray.length-1){
                        updateSql=updateSql+whereKeyName+" is null";
                    }else{
                        updateSql=updateSql+whereKeyName+" is null"+" "+whereJoinStrings.get(a)+" ";                       
                    }
                }else{
                    if(whereKeyValue instanceof Number){
                        if(a==whereKeyNameArray.length-1){
                            updateSql=updateSql+whereKeyName+"="+whereKeyValue.toString();
                        }else{
                            updateSql=updateSql+whereKeyName+"="+whereKeyValue.toString()+" "+whereJoinStrings.get(a)+" ";
                        }

                    }else if(whereKeyValue instanceof Date){
                        String dateValue=DateTransUtil.getDateString$yyyy_MM_dd_HH_mm_ss_SSS((Date)whereKeyValue);
                        if(a==whereKeyNameArray.length-1){
                            updateSql=updateSql+whereKeyName+"='"+dateValue+"'";
                        }else{
                            updateSql=updateSql+whereKeyName+"='"+dateValue+"' "+whereJoinStrings.get(a)+" ";
                        }
                    }else{
                        if(a==whereKeyNameArray.length-1){
                            updateSql=updateSql+whereKeyName+"='"+whereKeyValue.toString()+"'";
                        }else{
                            updateSql=updateSql+whereKeyName+"='"+whereKeyValue.toString()+"' "+whereJoinStrings.get(a)+" ";
                        }   
                    }
                }
                
            }
        }
        return updateSql;
    }
    
    private String getDeleteStatement(){
    	AssertUtil.Assert(!tableName.equals(""));
        String deleteSql="delete from "+tableName+" where ";
        int mapSize=whereKeyNamesAndValues.size();
        AssertUtil.Assert(mapSize>=1);
        if(mapSize==1){
            Set<String> whereKeyNameSet=whereKeyNamesAndValues.keySet();
            Iterator<String> it=whereKeyNameSet.iterator();
            String whereKeyName=it.next();
            Object whereKeyValue=whereKeyNamesAndValues.get(whereKeyName);
            if(whereKeyValue==null){
                deleteSql=deleteSql+whereKeyName+" is null";
            }else{
                if(whereKeyValue instanceof Number){
                    deleteSql=deleteSql+whereKeyName+"="+whereKeyValue.toString();
                }else if(whereKeyValue instanceof Date){
                    String dateValue=DateTransUtil.getDateString$yyyy_MM_dd_HH_mm_ss_SSS((Date)whereKeyValue);
                    deleteSql=deleteSql+whereKeyName+"='"+dateValue+"'";
                }else{
                    deleteSql=deleteSql+whereKeyName+"='"+whereKeyValue.toString()+"'";
                }
            }
            
        }else{
            int listSize=whereJoinStrings.size();
            if(listSize==0){
                for(int a=1;a<mapSize;a++){
                    whereJoinStrings.add("and");
                }
            }
            listSize=whereJoinStrings.size();
            AssertUtil.Assert(listSize==mapSize-1);
            Set<String> whereKeyNameSet=whereKeyNamesAndValues.keySet();
            Object[] whereKeyNameArray=whereKeyNameSet.toArray();
            for(int a=0;a<whereKeyNameArray.length;a++){
                String whereKeyName=(String)whereKeyNameArray[a];
                Object whereKeyValue=whereKeyNamesAndValues.get(whereKeyName);
                if(whereKeyValue==null){
                    if(a==whereKeyNameArray.length-1){
                        deleteSql=deleteSql+whereKeyName+" is null";
                    }else{
                        deleteSql=deleteSql+whereKeyName+" is null"+" "+whereJoinStrings.get(a)+" ";
                    }
                }else{
                    String valueType=whereKeyValue.getClass().getName();
                    if(whereKeyValue instanceof Number){
                        if(a==whereKeyNameArray.length-1){
                            deleteSql=deleteSql+whereKeyName+"="+whereKeyValue.toString();
                        }else{
                            deleteSql=deleteSql+whereKeyName+"="+whereKeyValue.toString()+" "+whereJoinStrings.get(a)+" ";
                        }

                    }else if(whereKeyValue instanceof Date){
                        String dateValue=DateTransUtil.getDateString$yyyy_MM_dd_HH_mm_ss_SSS((Date)whereKeyValue);
                        if(a==whereKeyNameArray.length-1){
                            deleteSql=deleteSql+whereKeyName+"='"+dateValue+"'";
                        }else{
                            deleteSql=deleteSql+whereKeyName+"='"+dateValue+"' "+whereJoinStrings.get(a)+" ";
                        }
                    }else{
                        if(a==whereKeyNameArray.length-1){
                            deleteSql=deleteSql+whereKeyName+"='"+whereKeyValue.toString()+"'";
                        }else{
                            deleteSql=deleteSql+whereKeyName+"='"+whereKeyValue.toString()+"' "+whereJoinStrings.get(a)+" ";
                        }
                    }
                }
                
            }
        }
        
        return deleteSql;
    }

    public String getTableName() {
        return tableName;
    }

    private void setTableName(String tableName) {
        this.tableName = tableName;
    }


    public ArrayList<String> getInsertOrUpdateKeyNames() {
        return insertOrUpdateKeyNames;
    }

    private void setInsertOrUpdateKeyNames(ArrayList<String> insertOrUpdateKeyNames) {
        this.insertOrUpdateKeyNames = insertOrUpdateKeyNames;
    }

    public ArrayList getInsertOrUpdateKeyValues() {
        return insertOrUpdateKeyValues;
    }

    private void setInsertOrUpdateKeyValues(ArrayList insertOrUpdateKeyValues) {
        this.insertOrUpdateKeyValues = insertOrUpdateKeyValues;
    }

    private String getInstanceFor() {
        return instanceFor;
    }

    private void setInstanceFor(String instanceFor) {
        this.instanceFor = instanceFor;
    }

    public HashMap<String, Object> getWhereKeyNamesAndValues() {
        return whereKeyNamesAndValues;
    }

    private void setWhereKeyNamesAndValues(HashMap<String, Object> whereKeyNamesAndValues) {
        this.whereKeyNamesAndValues = whereKeyNamesAndValues;
    }

    private ArrayList<String> getWhereJoinStrings() {
        return whereJoinStrings;
    }

    private void setWhereJoinStrings(ArrayList<String> whereJoinStrings) {
        this.whereJoinStrings = whereJoinStrings;
    }

    public String getSqlString() {
        if(sqlString==null){
            if(this.instanceFor.equals("insert")){
            	sqlString=this.getInsertStatement();
            }else if(this.instanceFor.equals("update")){
            	sqlString=this.getUpdateStatement();
            }else if(this.instanceFor.equals("delete")){
            	sqlString=this.getDeleteStatement();
            }else if(this.instanceFor.equals("oracleInsert")){
            	sqlString=this.getInsertStatement_Oracle();
            }else if(this.instanceFor.equals("oracleUpdate")){
            	sqlString=this.getUpdateStatement_Oracle();
            }else if(this.instanceFor.equals("oracleDelete")){
            	sqlString=this.getDeleteStatement_Oracle();
            }
        }  
        return sqlString;
    }

    private void setSqlString(String sqlStatement) {
        this.sqlString = sqlStatement;
    }
    public static SqlStatement getInstanceForOracleUpdate(String tableName,HashMap<String,Object> insertOrUpdateKeyNamesAndValues,String whereKeyName,Object whereKeyValue){
        SqlStatement dbp=new SqlStatement();
        dbp.setTableName(tableName);
        dbp.fromMapToArrayList(insertOrUpdateKeyNamesAndValues);
        HashMap<String,Object> hm=new HashMap<String,Object>();
        hm.put(whereKeyName, whereKeyValue);
        dbp.setWhereKeyNamesAndValues(hm);
        dbp.setInstanceFor("oracleUpdate");
        dbp.setSqlString(dbp.getUpdateStatement_Oracle());
        return dbp;
    }
    
    public static SqlStatement getInstanceForOracleUpdate(String tableName,HashMap<String,Object> insertOrUpdateKeyNamesAndValues,HashMap<String,Object> whereKeyNamesAndValues){
        SqlStatement dbp=new SqlStatement();
        dbp.setTableName(tableName);
        dbp.fromMapToArrayList(insertOrUpdateKeyNamesAndValues);
        dbp.setWhereKeyNamesAndValues(whereKeyNamesAndValues);
        dbp.setInstanceFor("oracleUpdate");
        dbp.setSqlString(dbp.getUpdateStatement_Oracle());
        return dbp;
    }
    public static SqlStatement getInstanceForOracleUpdate(String tableName,HashMap<String,Object> insertOrUpdateKeyNamesAndValues,HashMap<String,Object> whereKeyNamesAndValues,ArrayList<String> whereJoinStrings){
        SqlStatement dbp=new SqlStatement();
        dbp.setTableName(tableName);
        dbp.fromMapToArrayList(insertOrUpdateKeyNamesAndValues);
        dbp.setWhereKeyNamesAndValues(whereKeyNamesAndValues);
        dbp.setWhereJoinStrings(whereJoinStrings);
        dbp.setInstanceFor("oracleUpdate");
        dbp.setSqlString(dbp.getUpdateStatement_Oracle());
        return dbp;
    }
    public static SqlStatement getInstanceForOracleInsert(String tableName,HashMap<String,Object> insertOrUpdateKeyNamesAndValues){
        SqlStatement dbp=new SqlStatement();
        dbp.setTableName(tableName);
        dbp.fromMapToArrayList(insertOrUpdateKeyNamesAndValues);
        dbp.setInstanceFor("oracleInsert");
        dbp.setSqlString(dbp.getInsertStatement_Oracle());
        return dbp;
    }
    public static SqlStatement getInstanceForOracleDelete(String tableName,String whereKeyName,Object whereKeyValue){
        SqlStatement dbp=new SqlStatement();
        dbp.setTableName(tableName);
        HashMap<String,Object> hm=new HashMap<String,Object>();
        hm.put(whereKeyName, whereKeyValue);
        dbp.setWhereKeyNamesAndValues(hm);
        dbp.setInstanceFor("oracleDelete");
        dbp.setSqlString(dbp.getDeleteStatement_Oracle());
        return dbp;
    }
    public static SqlStatement getInstanceForOracleDelete(String tableName,HashMap<String,Object> whereKeyNamesAndValues){
        SqlStatement dbp=new SqlStatement();
        dbp.setTableName(tableName);
        dbp.setWhereKeyNamesAndValues(whereKeyNamesAndValues);
        dbp.setInstanceFor("oracleDelete");
        dbp.setSqlString(dbp.getDeleteStatement_Oracle());
        return dbp;
    }
    public static SqlStatement getInstanceForOracleDelete(String tableName,HashMap<String,Object> whereKeyNamesAndValues,ArrayList<String> whereJoinStrings){
        SqlStatement dbp=new SqlStatement();
        dbp.setTableName(tableName);
        dbp.setWhereKeyNamesAndValues(whereKeyNamesAndValues);
        dbp.setWhereJoinStrings(whereJoinStrings);
        dbp.setInstanceFor("oracleDelete");
        dbp.setSqlString(dbp.getDeleteStatement_Oracle());
        return dbp;
    }
    private String getInsertStatement_Oracle(){
    	AssertUtil.Assert(insertOrUpdateKeyNames.size()>0&&insertOrUpdateKeyNames.size()==insertOrUpdateKeyValues.size());
    	AssertUtil.Assert(!tableName.equals(""));
        String insertSql="insert into "+tableName+"(";
        String fieldsSql="";
        for(int n=0;n<insertOrUpdateKeyNames.size();n++){
            String keyName=insertOrUpdateKeyNames.get(n);
            if(n==insertOrUpdateKeyNames.size()-1){
                fieldsSql=fieldsSql+keyName;
            }else{
                fieldsSql=fieldsSql+keyName+",";
            }
        }
        insertSql=insertSql+fieldsSql+") values(";
        String valuesSql="";
        for(int v=0;v<insertOrUpdateKeyValues.size();v++){ 
            String keyValue="";
            Object value=insertOrUpdateKeyValues.get(v);
            if(value==null){
                keyValue="null";
                if(v==insertOrUpdateKeyValues.size()-1){
                    valuesSql=valuesSql+keyValue;
                }else{
                    valuesSql=valuesSql+keyValue+",";
                }
            }else if(value instanceof Number){
                keyValue=value.toString();
                if(v==insertOrUpdateKeyValues.size()-1){
                    valuesSql=valuesSql+keyValue;
                }else{
                    valuesSql=valuesSql+keyValue+",";
                }
            }else if(value instanceof Date){
                keyValue=DateTransUtil.getDateString$yyyy_MM_dd_HH_mm_ss((Date)value);
                if(v==insertOrUpdateKeyValues.size()-1){
                    valuesSql=valuesSql+"to_date('"+keyValue+"','yyyy-mm-dd hh24:mi:ss')";
                }else{
                    valuesSql=valuesSql+"to_date('"+keyValue+"','yyyy-mm-dd hh24:mi:ss'),";
                }
            }else{
                keyValue=value.toString();
                if(v==insertOrUpdateKeyValues.size()-1){
                    valuesSql=valuesSql+"'"+keyValue+"'";
                }else{
                    valuesSql=valuesSql+"'"+keyValue+"',";
                }
            }
                 
        }
        insertSql=insertSql+valuesSql+")"; 
        return insertSql;
    }
    private String getUpdateStatement_Oracle(){
    	AssertUtil.Assert(insertOrUpdateKeyNames.size()>0&&insertOrUpdateKeyNames.size()==insertOrUpdateKeyValues.size());
    	AssertUtil.Assert(!tableName.equals(""));
        String updateSql="update "+tableName+" set ";
        String setSql="";
        for(int s=0;s<insertOrUpdateKeyNames.size();s++){
            String keyName=insertOrUpdateKeyNames.get(s);
            String keyValue="";
            Object value=insertOrUpdateKeyValues.get(s);
            if(value==null){
                keyValue="null";
                if(s==insertOrUpdateKeyNames.size()-1){
                    setSql=setSql+keyName+"="+keyValue;
                }else{
                    setSql=setSql+keyName+"="+keyValue+",";
                }
            }else if(value instanceof Number){
                keyValue=value.toString();
                if(s==insertOrUpdateKeyNames.size()-1){
                    setSql=setSql+keyName+"="+keyValue;
                }else{
                    setSql=setSql+keyName+"="+keyValue+",";
                }
            }else if(value instanceof Date){
                keyValue=DateTransUtil.getDateString$yyyy_MM_dd_HH_mm_ss((Date)value);
                if(s==insertOrUpdateKeyNames.size()-1){
                    setSql=setSql+keyName+"="+"to_date('"+keyValue+"','yyyy-mm-dd hh24:mi:ss')";
                }else{
                    setSql=setSql+keyName+"="+"to_date('"+keyValue+"','yyyy-mm-dd hh24:mi:ss'),";
                }
            }else{
                keyValue=value.toString();
                if(s==insertOrUpdateKeyNames.size()-1){
                    setSql=setSql+keyName+"='"+keyValue+"'";
                }else{
                    setSql=setSql+keyName+"='"+keyValue+"',";
                }
            } 
            
        }
        updateSql=updateSql+setSql+" where ";
        int mapSize=whereKeyNamesAndValues.size();
        AssertUtil.Assert(mapSize>=1);
        if(mapSize==1){
            Set<String> whereKeyNameSet=whereKeyNamesAndValues.keySet();
            Iterator<String> it=whereKeyNameSet.iterator();
            String whereKeyName=it.next();
            Object whereKeyValue=whereKeyNamesAndValues.get(whereKeyName);
            if(whereKeyValue==null){
                updateSql=updateSql+whereKeyName+" is null";
            }else{
                if(whereKeyValue instanceof Number){
                    updateSql=updateSql+whereKeyName+"="+whereKeyValue.toString();
                }else if(whereKeyValue instanceof Date){
                    String dateValue=DateTransUtil.getDateString$yyyy_MM_dd_HH_mm_ss((Date)whereKeyValue);
                    updateSql=updateSql+whereKeyName+"="+"to_date('"+dateValue+"','yyyy-mm-dd hh24:mi:ss')";
                }else{
                    updateSql=updateSql+whereKeyName+"='"+whereKeyValue.toString()+"'";
                }
            }
            
        }else{
            int listSize=whereJoinStrings.size();
            if(listSize==0){
                for(int a=1;a<mapSize;a++){
                    whereJoinStrings.add("and");
                }
            }
            listSize=whereJoinStrings.size();
            assert(listSize==mapSize-1);
            Set<String> whereKeyNameSet=whereKeyNamesAndValues.keySet();
            Object[] whereKeyNameArray=whereKeyNameSet.toArray();
            for(int a=0;a<whereKeyNameArray.length;a++){
                String whereKeyName=(String)whereKeyNameArray[a];
                Object whereKeyValue=whereKeyNamesAndValues.get(whereKeyName);
                if(whereKeyValue==null){
                    if(a==whereKeyNameArray.length-1){
                        updateSql=updateSql+whereKeyName+" is null";
                    }else{
                        updateSql=updateSql+whereKeyName+" is null"+" "+whereJoinStrings.get(a)+" ";                       
                    }
                }else{
                        if(whereKeyValue instanceof Number){
                            if(a==whereKeyNameArray.length-1){
                                updateSql=updateSql+whereKeyName+"="+whereKeyValue.toString();
                            }else{
                                updateSql=updateSql+whereKeyName+"="+whereKeyValue.toString()+" "+whereJoinStrings.get(a)+" ";
                            }
                        }else if(whereKeyValue instanceof Date){
                            String dateValue=DateTransUtil.getDateString$yyyy_MM_dd_HH_mm_ss((Date)whereKeyValue);
                            if(a==whereKeyNameArray.length-1){
                                updateSql=updateSql+whereKeyName+"="+"to_date('"+dateValue+"','yyyy-mm-dd hh24:mi:ss')";
                            }else{
                                updateSql=updateSql+whereKeyName+"="+"to_date('"+dateValue+"','yyyy-mm-dd hh24:mi:ss') "+whereJoinStrings.get(a)+" ";
                            }

                        }else{
                            if(a==whereKeyNameArray.length-1){
                                updateSql=updateSql+whereKeyName+"='"+whereKeyValue.toString()+"'";
                            }else{
                                updateSql=updateSql+whereKeyName+"='"+whereKeyValue.toString()+"' "+whereJoinStrings.get(a)+" ";
                            }

                        }
                }
     
            }
        }
        return updateSql;
    }
    
    private String getDeleteStatement_Oracle(){
    	AssertUtil.Assert(!tableName.equals(""));
        String deleteSql="delete from "+tableName+" where ";
        int mapSize=whereKeyNamesAndValues.size();
        AssertUtil.Assert(mapSize>=1);
        if(mapSize==1){
            Set<String> whereKeyNameSet=whereKeyNamesAndValues.keySet();
            Iterator<String> it=whereKeyNameSet.iterator();
            String whereKeyName=it.next();
            Object whereKeyValue=whereKeyNamesAndValues.get(whereKeyName);
            if(whereKeyValue==null){
                deleteSql=deleteSql+whereKeyName+" is null";
            }else{             
                if(whereKeyValue instanceof Number){
                    deleteSql=deleteSql+whereKeyName+"="+whereKeyValue.toString();
                }else if(whereKeyValue instanceof Date){
                    String dateValue=DateTransUtil.getDateString$yyyy_MM_dd_HH_mm_ss((Date)whereKeyValue);
                    deleteSql=deleteSql+whereKeyName+"="+"to_date('"+dateValue+"','yyyy-mm-dd hh24:mi:ss')";
                }else{
                    deleteSql=deleteSql+whereKeyName+"='"+whereKeyValue.toString()+"'";
                }
            }
            
        }else{
            int listSize=whereJoinStrings.size();
            if(listSize==0){
                for(int a=1;a<mapSize;a++){
                    whereJoinStrings.add("and");
                }
            }
            listSize=whereJoinStrings.size();
            AssertUtil.Assert(listSize==mapSize-1);
            Set<String> whereKeyNameSet=whereKeyNamesAndValues.keySet();
            Object[] whereKeyNameArray=whereKeyNameSet.toArray();
            for(int a=0;a<whereKeyNameArray.length;a++){
                String whereKeyName=(String)whereKeyNameArray[a];
                Object whereKeyValue=whereKeyNamesAndValues.get(whereKeyName);
                if(whereKeyValue==null){
                    if(a==whereKeyNameArray.length-1){
                        deleteSql=deleteSql+whereKeyName+" is null";
                    }else{
                        deleteSql=deleteSql+whereKeyName+" is null"+" "+whereJoinStrings.get(a)+" ";
                    }
                }else{
                        
                        if(whereKeyValue instanceof Number){
                            if(a==whereKeyNameArray.length-1){
                                deleteSql=deleteSql+whereKeyName+"="+whereKeyValue.toString();
                            }else{
                                deleteSql=deleteSql+whereKeyName+"="+whereKeyValue.toString()+" "+whereJoinStrings.get(a)+" ";
                            }

                        }else if(whereKeyValue instanceof Date){
                            String dateValue=DateTransUtil.getDateString$yyyy_MM_dd_HH_mm_ss((Date)whereKeyValue);
                            if(a==whereKeyNameArray.length-1){
                                deleteSql=deleteSql+whereKeyName+"="+"to_date('"+dateValue+"','yyyy-mm-dd hh24:mi:ss')";
                            }else{
                                deleteSql=deleteSql+whereKeyName+"="+"to_date('"+dateValue+"','yyyy-mm-dd hh24:mi:ss') "+whereJoinStrings.get(a)+" ";
                            }

                        }else{
                            if(a==whereKeyNameArray.length-1){
                                deleteSql=deleteSql+whereKeyName+"='"+whereKeyValue.toString()+"'";
                            }else{
                                deleteSql=deleteSql+whereKeyName+"='"+whereKeyValue.toString()+"' "+whereJoinStrings.get(a)+" ";
                            }

                        }
                }

            }
        }
        
        return deleteSql;
    }

}
