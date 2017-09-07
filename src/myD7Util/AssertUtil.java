package myD7Util;

public class AssertUtil {
	public static void Assert(boolean isTrue) throws RuntimeException{
		if(!isTrue){
			throw new RuntimeException("AssertUtil.Assert()抛出断言异常!");
		}
	}
	public static void Assert(boolean isTrue,String exceptionMessage) throws RuntimeException{
		if(!isTrue){
			throw new RuntimeException(exceptionMessage);
		}
	}
	
	public static void main(String[] args){
		int a=1;
		Assert(a>1);
		System.out.println("上面抛出异常了吗?");
	}
}
