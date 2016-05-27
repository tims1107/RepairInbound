package utils;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import model.TestMap;

public class ParseUtil {
	
	public static enum TranType {
		OSU(1),POST(2),ADD(3),CANCEL(5);
		public  int value;
		
		TranType(int value){
			this.value = value;
		}
	}
	
	public static String expandMSH(String line, String cid,String reqnum) {
		
		StringBuilder sb = new StringBuilder(0);
		StringBuilder grp = new StringBuilder(0);
		
		sb.append(line);
		String append;
		
		Map <Integer,MatchResult> mtch = new TreeMap<Integer,MatchResult>();
		Map <Integer,MatchResult> inFind = new TreeMap<Integer,MatchResult>();
		
		int count = 0;
		
		
		try {
			
			Pattern Regex = Pattern.compile("(.*?)(\\|)|(.*?\r)",Pattern.CANON_EQ);
			
			Pattern findGroup = Pattern.compile("(.*?)(\\^)",Pattern.CANON_EQ);
			
			
			
			Matcher m = Regex.matcher(line);

			//Find OBR matches and load map
			while (m.find()) {
				
				mtch.put(count++, m.toMatchResult() );
			}
			
			Matcher g = findGroup.matcher(mtch.get(4).group(1));
			
			count = 0;
			
			//Load map with testcode groups ^
			while(g.find()){
				inFind.put(count++, g.toMatchResult());
			}

			for(int n : inFind.keySet()){
				grp.append(inFind.get(n).group());
				
			}
			
			
			sb.replace(mtch.get(6).start(1), mtch.get(6).end(1), fmtdate());
			sb.insert(mtch.get(3).start(1), cid + "^");	
			
			

		} catch (PatternSyntaxException ex) {
			ex.printStackTrace();
		}
		
		
		return sb.toString();
	}
	
    public static String renumberOBR(String line, int num) {
		
		StringBuilder sb = new StringBuilder(0);
		StringBuilder grp = new StringBuilder(0);
		
		sb.append(line);
		String append;
		
		Map <Integer,MatchResult> mtch = new TreeMap<Integer,MatchResult>();
		Map <Integer,MatchResult> inFind = new TreeMap<Integer,MatchResult>();
		
		int count = 0;
		
		
		try {
			
			Pattern Regex = Pattern.compile("(.*?)(\\|)|(.*?\r)",Pattern.CANON_EQ);
			
			Pattern findGroup = Pattern.compile("(.*?)(\\^)",Pattern.CANON_EQ);
			
			
			
			Matcher m = Regex.matcher(line);

			//Find OBR matches and load map
			while (m.find()) {
				
				mtch.put(count++, m.toMatchResult() );
			}
			
			Matcher g = findGroup.matcher(mtch.get(4).group(1));
			
			System.out.println(mtch.get(1).group(1));
			
			
//			count = 0;
//			
//			//Load map with testcode groups ^
//			while(g.find()){
//				inFind.put(count++, g.toMatchResult());
//			}
//
//			for(int n : inFind.keySet()){
//				grp.append(inFind.get(n).group());
//				
//			}
//			
//			
			sb.replace(mtch.get(1).start(1), mtch.get(1).end(1), Integer.toString(num));
//			sb.insert(mtch.get(3).start(1), cid + "^");	
//			
//			

		} catch (PatternSyntaxException ex) {
			ex.printStackTrace();
		}
		
		
		return sb.toString();
	}


	private static String fmtdate(){
	
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMddHHmmss");
	
		return formatter.format(Calendar.getInstance().getTime());
	
	}
	
	public static String expandOBR(String line,TestMap tm,String reqnum) {
		
		StringBuilder sb = new StringBuilder(0);
		StringBuilder grp = new StringBuilder(0);
		
		sb.append(line);
		String append;
		
		Map <Integer,MatchResult> mtch = new TreeMap<Integer,MatchResult>();
		Map <Integer,MatchResult> inFind = new TreeMap<Integer,MatchResult>();
		
		int count = 0;
		
		
		try {
			
			Pattern Regex = Pattern.compile("(.*?)(\\|)|(.*?\r)",Pattern.CANON_EQ);
			
			Pattern findGroup = Pattern.compile("(.*?)(\\^)|(.*[^\\^])",Pattern.CANON_EQ);
			
			
			
			Matcher m = Regex.matcher(line);

			//Find OBR matches and load map
			while (m.find()) {
				
				mtch.put(count++, m.toMatchResult() );
			}
			
			Matcher g = findGroup.matcher(mtch.get(4).group(1));
			
			
			
			count = 0;
			
			//Load map with testcode groups ^
			while(g.find()){
				inFind.put(count++, g.toMatchResult());
			}
			
			

			for(int n : inFind.keySet()){
				grp.append(inFind.get(n).group());
								
			}
			
			
			
			
			//Replace testcode and test description
			grp.replace(inFind.get(1).start(1), inFind.get(1).end(1), tm.getTestdesc());
			grp.replace(inFind.get(0).start(1), inFind.get(0).end(1), tm.getTestcode());
			
			//Container abbrev						
			sb.replace(mtch.get(20).start(1), mtch.get(20).end(1), tm.getContainerabbrev());
			
			//Test code
			sb.replace(mtch.get(4).start(1), mtch.get(4).end(1), grp.toString());
			
			//Accession type and reqquistion
			sb.replace(mtch.get(3).start(1), mtch.get(3).end(1), tm.getTesttype().trim() + reqnum);
			
			//Insert from start accession type and reqnum trailing ^
			sb.insert(mtch.get(2).start(1),tm.getTesttype().trim() + reqnum + "^");
			
			

		} catch (PatternSyntaxException ex) {
			ex.printStackTrace();
		}
		
		
		return sb.toString();
	}

	
	public static String expandORC(String line, String reqnum) {
		
		StringBuilder sb = new StringBuilder(0);
		StringBuilder grp = new StringBuilder(0);
		
		sb.append(line);
		String append;
		
		Map <Integer,MatchResult> mtch = new TreeMap<Integer,MatchResult>();
		Map <Integer,MatchResult> inFind = new TreeMap<Integer,MatchResult>();
		
		int count = 0;
		
		
		try {
			
			Pattern Regex = Pattern.compile("(.*?)(\\|)|(.*?\r)",Pattern.CANON_EQ);
			
			Pattern findGroup = Pattern.compile("(.*?)(\\^)",Pattern.CANON_EQ);
			
			
			
			Matcher m = Regex.matcher(line);

			//Find OBR matches and load map
			while (m.find()) {
				
				mtch.put(count++, m.toMatchResult() );
			}
			
			Matcher g = findGroup.matcher(mtch.get(4).group(1));
			
			count = 0;
			
			//Load map with testcode groups ^
			while(g.find()){
				inFind.put(count++, g.toMatchResult());
			}

			for(int n : inFind.keySet()){
				grp.append(inFind.get(n).group());
				
			}
			
			
			sb.replace(mtch.get(2).start(1), mtch.get(2).end(1), reqnum);
			
			
			

		} catch (PatternSyntaxException ex) {
			ex.printStackTrace();
		}
		
		
		return sb.toString();
	}


	
	public static List<String> getFields(String line){
	
		List<String> matchList = new ArrayList<String>();
		int count = 0;
		try {
			Pattern regex = Pattern.compile("(?:\\^FD)([^\\^]*)(?:\\^FS)", Pattern.DOTALL | Pattern.MULTILINE);
			Matcher regexMatcher = regex.matcher(line);
			while (regexMatcher.find()) {
			
				matchList.add(regexMatcher.group(1));
				
				
			
				
			} 
			
			return matchList;
			
		} catch (PatternSyntaxException ex) {
			// Syntax error in the regular expression
		}
		
		return null;
	}
	
	public static String getSource(String line){
		
		String source = null;
		int count = 0;
		try {
			Pattern regex = Pattern.compile("(?sm)(\\x0B)([^\\x1C].*)(\\x1C\\x0D|\\x1C)", Pattern.DOTALL | Pattern.MULTILINE);
			Matcher regexMatcher = regex.matcher(line);
			if (regexMatcher.find()) {
			
				source = regexMatcher.group(2);
				
				
			
				
			} 
			
			return source;
			
		} catch (PatternSyntaxException ex) {
			// Syntax error in the regular expression
		}
		
		return null;
	}

	public static List<String> splitLine(String line){
		List<String> matchList = new ArrayList<String>();
		int count = 0;
		try {
			Pattern regex = Pattern.compile("([^\\|]*)?(\\|)?", Pattern.DOTALL | Pattern.MULTILINE);
			Matcher regexMatcher = regex.matcher(line);
			while (regexMatcher.find()) {
			
				matchList.add(regexMatcher.group(1));
				
				if(regexMatcher.group(2) == null) break;
			
				
			} 
			
			return matchList;
			
		} catch (PatternSyntaxException ex) {
			// Syntax error in the regular expression
		}
		
		return null;
	
		
	}
	
	
	public static List<String> splitField(String line){
		List<String> matchList = new ArrayList<String>();
		int count = 0;
		try {
			Pattern regex = Pattern.compile("([^\\^]*)?(\\^)?", Pattern.DOTALL | Pattern.MULTILINE);
			Matcher regexMatcher = regex.matcher(line);
			while (regexMatcher.find()) {
			
				matchList.add(regexMatcher.group(1));
				if(regexMatcher.group(2) == null) break;
			} 
			
			return matchList;
			
		} catch (PatternSyntaxException ex) {
			// Syntax error in the regular expression
		}
		
		return null;
		
	}
	
	public static String getPart(String pattern,String line){
		
		try {
			Pattern regex = Pattern.compile(pattern, Pattern.DOTALL | Pattern.MULTILINE);
			Matcher regexMatcher = regex.matcher(line);
			if (regexMatcher.find()) {
			
				return regexMatcher.group(1);
				
			} 
			
		} catch (PatternSyntaxException ex) {
			ex.printStackTrace();
		}
		
		return null;
	
	}
	
	public static List<List<String>> parseLines(String source){
		List<List<String>> sourcelist = new ArrayList<List<String>>();
		String line;
		List<String> lines =  new ArrayList<String>();
		
		Scanner scan = new Scanner(source);
		while(scan.hasNextLine()){
			line = scan.nextLine() + "\r";
			
			try
			{
			if(Pattern.compile("(MSH|PID|PV1|ORC)").matcher(line).find()){
				lines.add(line);
				if(line.startsWith("ORC")){
					sourcelist.add(lines);
					lines = new ArrayList<String>();
				}
				
			} else if (Pattern.compile("(OBR)").matcher(line).find()){
				
					if(lines.size() > 0){
						
						sourcelist.add(lines);
						
						lines = new ArrayList<String>();
					}
										
					lines.add(line);
					
			
				
			}	else if (Pattern.compile("(DG1|OBX)").matcher(line).find()){
				lines.add(line);
			}
			
			} catch (Exception e){
				
				e.printStackTrace();
			}
				
			
		}
		scan.close();
		
		if(lines.size() > 0) {
			//System.out.println(sourcelist);
			sourcelist.add(lines);
			return sourcelist;
		} 
		
		return null;
		
		
	}


}
