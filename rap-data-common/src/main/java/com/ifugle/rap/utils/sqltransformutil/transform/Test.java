//package com.ifugle.rap.utils.sqltransformutil.transform;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * @author Minc
// * @date 2021/12/31 16:35
// */
//public class Test {
//    public static void main(String[] args) throws Exception {
//        String sql = "select xnzz_id,bm_id,count(*) from user_all_tag group by xnzz_id,bm_id limit 10";
//        List<String> list = appendKeyWord(sql);
//
//        extracted(list);
//        System.out.println();
//    }
//
//    private static SqlEntry extracted(List<String> list) throws Exception {
//        SqlEntry sqlEntry = new SqlEntry();
//        boolean throwException = true;
//        for (int i = 0; i < list.size() - 1; i++) {
//            throwException = false;
//            DataType fitSqlDataType = sqlEntry.getFitSqlDataType(list.get(i));
//            if (fitSqlDataType != null) {
//                fitSqlDataType.setValue(list.get(i + 1));
//            }
//        }
//        if (throwException) {
//            throw new Exception("找不到关键字！请检查sql！");
//        } else {
//            return sqlEntry;
//        }
//    }
//
//    //合并关键字，类似于group by或者order by
//    private static List<String> appendKeyWord(String sql) {
//        List<String> sqlParts = getSqlPart(sql);
//        for (int i = 0; i < sqlParts.size(); i++) {
//            if (KeyWord.group_by.getKeyword().startsWith(sqlParts.get(i)) || KeyWord.order_by.getKeyword().startsWith(sqlParts.get(i))) {
//                if (sqlParts.get(i + 1).equals("by")) {
//                    String remove = sqlParts.remove(i + 1);
//                    sqlParts.set(i, sqlParts.get(i) + " " + remove);
//                }
//            }
//        }
//        return sqlParts;
//    }
//
//    private static List<String> getSqlPart(String sql) {
//        return Arrays.stream(sql.split(" ", -1)).filter(x -> !x.equals("")).collect(Collectors.toList());
//    }
//}
