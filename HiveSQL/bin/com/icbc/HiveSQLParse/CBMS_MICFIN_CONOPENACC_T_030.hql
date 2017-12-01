******************************
hiveql_author:kfzx-caijh
create_date:2017-03-21
last_modified_by:kfzx-caijh
last_modify_date:2017-03-21
job_name:CBMS_MICFIN_CONOPENACC_T_030
sum_needed_tables: DCM_CBS_MSC_QYT_BUSINESS_INF_S��DCM_BIM_NASEACCPTINFO_DTL_S��DCM_CBS_EDW_MSC_BRANCH_S��DCM_CBS_MSC_C_BRCHRECS_S
hiveql_etl_system: F-CBMS_000001
hiveql_description:ͳ�Ʊ�ݿ�������
******************************

set mapreduce.job.queuename=QueueU;

use BDSP_CBMS${version_num};

DROP TABLE IF EXISTS BDSP_CBMS${version_num}.CBMS_MICRO_CONOPENACC_TMP1;
CREATE TABLE IF NOT EXISTS BDSP_CBMS${version_num}.CBMS_MICRO_CONOPENACC_TMP1
(
 INT_ORG_ZONE             STRING      COMMENT '������'            
,INT_ORG_BRCH             STRING      COMMENT '�����' 
,INT_FREQ                 INT      COMMENT 'Ƶ��'           
,ENTPONE_APPNT_CNT        DECIMAL(17,0)   COMMENT '��ҵͨԤԼ��'
,HDLED_BUS_LINSE          DECIMAL(17,0)   COMMENT '�Ѱ���Ӫҵִ����'
,ENTPONE_OPENACC_CNT      DECIMAL(17,0)   COMMENT '��ҵͨ�û�������'
,APPNT_OPENACC_CNT        DECIMAL(17,0)   COMMENT 'ԤԼ������' 
,APPNT_OPENACCSUCC_CNT    DECIMAL(17,0)   COMMENT 'ԤԼ�ɹ�������'            
)
COMMENT '��ݿ���ͳ���м��'
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\27'
STORED AS RCFILE;


--Ƶ�ȣ�0-�գ�1-�£�2-����3-��
--ͳ�Ʊ�������ݿ���ҵ������(0-��)
INSERT OVERWRITE TABLE CBMS_MICRO_CONOPENACC_TMP1
  SELECT T0.ZONE_ID,
         T0.BRCH_ID,
         0,
         IF(T1.ENTPONE_APPNT_CNT IS NULL,0,T1.ENTPONE_APPNT_CNT),
         IF(T2.HDLED_BUS_LINSE IS NULL,0,T2.HDLED_BUS_LINSE),
         IF(T3.ENTPONE_OPENACC_CNT IS NULL,0,T3.ENTPONE_OPENACC_CNT),
         IF(T4.APPNT_OPENACC_CNT IS NULL,0,T4.APPNT_OPENACC_CNT),
         IF(T5.APPNT_OPENACCSUCC_CNT IS NULL,0,T5.APPNT_OPENACCSUCC_CNT)
    FROM (SELECT LPAD(ZONE_ID,5,'0') ZONE_ID, 
                 LPAD(BRCH_ID,5,'0') BRCH_ID, 
                 STRU_ID
           FROM CBS${version_num}.DCM_CBS_EDW_MSC_BRANCH_S
          WHERE pt_dt = '${process_date}') T0
         
           LEFT OUTER JOIN
         
          (SELECT STRU_ID, 
                  COUNT(APPLY_NO) AS ENTPONE_APPNT_CNT
             FROM CBS${version_num}.DCM_CBS_MSC_QYT_BUSINESS_INF_S
            WHERE pt_dt = '${process_date}'
              AND DATA_TYPE = '1'
              AND CREAT_DATE = regexp_replace(pt_dt,'-','')
            GROUP BY STRU_ID) T1
         
             ON T0.STRU_ID = T1.STRU_ID
         
           LEFT OUTER JOIN
         
          (SELECT STRU_ID, 
                  COUNT(APPLY_NO) AS HDLED_BUS_LINSE
             FROM CBS${version_num}.DCM_CBS_MSC_QYT_BUSINESS_INF_S
            WHERE pt_dt = '${process_date}'
              AND DATA_TYPE = '1'
              AND STATUS IN ('2', '3', '4')
            GROUP BY STRU_ID) T2
         
             ON T0.STRU_ID = T2.STRU_ID
         
           LEFT OUTER JOIN
         
          (SELECT STRU_ID, 
                  COUNT(APPLY_NO) AS ENTPONE_OPENACC_CNT
             FROM CBS${version_num}.DCM_CBS_MSC_QYT_BUSINESS_INF_S
            WHERE pt_dt = '${process_date}'
              AND DATA_TYPE = '1'
              AND STATUS = '4'
            GROUP BY STRU_ID) T3
         
             ON T0.STRU_ID = T3.STRU_ID
         
           LEFT OUTER JOIN
         
          (SELECT LPAD(ZONENO,5,'0') ZONENO, 
                  LPAD(BRNO,5,'0') BRNO, 
                  COUNT(APPLNO) AS APPNT_OPENACC_CNT
             FROM BIM${version_num}.DCM_BIM_NASEACCPTINFO_DTL_S
            WHERE pt_dt = '${process_date}'
              AND CHANNELID IN ('4', '5', '6', '7', '8')
              AND REG_DATE = pt_dt
            GROUP BY ZONENO, BRNO) T4
         
             ON T0.ZONE_ID = T4.ZONENO
            AND T0.BRCH_ID = T4.BRNO
         
           LEFT OUTER JOIN
         
          (SELECT LPAD(ZONENO,5,'0') ZONENO, 
                  LPAD(BRNO,5,'0') BRNO,
                  COUNT(APPLNO) AS APPNT_OPENACCSUCC_CNT
             FROM BIM${version_num}.DCM_BIM_NASEACCPTINFO_DTL_S
            WHERE pt_dt = '${process_date}'
              AND CHANNELID IN ('4', '5', '6', '7', '8')
              AND STATUS in ('4')
              AND OPENEDACCNO <> ""
              AND TAKE_DATE = pt_dt
            GROUP BY ZONENO, BRNO) T5
         
             ON T0.ZONE_ID = T5.ZONENO
            AND T0.BRCH_ID = T5.BRNO
;

--Ƶ�ȣ�0-�գ�1-�£�2-����3-��
--ͳ�Ʊ�������ݿ���ҵ������(1-��)
INSERT INTO TABLE CBMS_MICRO_CONOPENACC_TMP1
  SELECT T0.ZONE_ID,
         T0.BRCH_ID,
         1,
         IF(T1.ENTPONE_APPNT_CNT IS NULL,0,T1.ENTPONE_APPNT_CNT),
         IF(T2.HDLED_BUS_LINSE IS NULL,0,T2.HDLED_BUS_LINSE),
         IF(T3.ENTPONE_OPENACC_CNT IS NULL,0,T3.ENTPONE_OPENACC_CNT),
         IF(T4.APPNT_OPENACC_CNT IS NULL,0,T4.APPNT_OPENACC_CNT),
         IF(T5.APPNT_OPENACCSUCC_CNT IS NULL,0,T5.APPNT_OPENACCSUCC_CNT)
    FROM (SELECT LPAD(ZONE_ID,5,'0') ZONE_ID, 
                 LPAD(BRCH_ID,5,'0') BRCH_ID,
                 STRU_ID
           FROM CBS${version_num}.DCM_CBS_EDW_MSC_BRANCH_S
          WHERE pt_dt = '${process_date}') T0
         
           LEFT OUTER JOIN
         
          (SELECT STRU_ID, 
                  COUNT(APPLY_NO) AS ENTPONE_APPNT_CNT
             FROM CBS${version_num}.DCM_CBS_MSC_QYT_BUSINESS_INF_S
            WHERE pt_dt <= '${process_date}'
              AND pt_dt >= udf.firstDayOfMonth('${process_date}')
              AND DATA_TYPE = '1'
              AND CREAT_DATE = regexp_replace(pt_dt,'-','')
            GROUP BY STRU_ID) T1
         
             ON T0.STRU_ID = T1.STRU_ID
         
           LEFT OUTER JOIN
         
          (SELECT STRU_ID, 
                  COUNT(DISTINCT APPLY_NO) AS HDLED_BUS_LINSE
             FROM CBS${version_num}.DCM_CBS_MSC_QYT_BUSINESS_INF_S
            WHERE pt_dt <= '${process_date}'
              AND pt_dt >= udf.firstDayOfMonth('${process_date}')
              AND DATA_TYPE = '1'
              AND STATUS IN ('2', '3', '4')
            GROUP BY STRU_ID) T2
         
             ON T0.STRU_ID = T2.STRU_ID
         
           LEFT OUTER JOIN
         
          (SELECT STRU_ID, 
                  COUNT(DISTINCT APPLY_NO) AS ENTPONE_OPENACC_CNT
             FROM CBS${version_num}.DCM_CBS_MSC_QYT_BUSINESS_INF_S
            WHERE pt_dt <= '${process_date}'
              AND pt_dt >= udf.firstDayOfMonth('${process_date}')
              AND DATA_TYPE = '1'
              AND STATUS = '4'
            GROUP BY STRU_ID) T3
         
             ON T0.STRU_ID = T3.STRU_ID
         
           LEFT OUTER JOIN
         
          (SELECT LPAD(ZONENO,5,'0') ZONENO, 
                  LPAD(BRNO,5,'0') BRNO,
                  COUNT(APPLNO) AS APPNT_OPENACC_CNT
             FROM BIM${version_num}.DCM_BIM_NASEACCPTINFO_DTL_S
            WHERE CHANNELID IN ('4', '5', '6', '7', '8')
              AND REG_DATE <= '${process_date}'
              AND REG_DATE >= udf.firstDayOfMonth('${process_date}')
              AND pt_dt = '${process_date}'
            GROUP BY ZONENO, BRNO) T4
         
             ON T0.ZONE_ID = T4.ZONENO
            AND T0.BRCH_ID = T4.BRNO
         
           LEFT OUTER JOIN
         
          (SELECT LPAD(ZONENO,5,'0') ZONENO, 
                  LPAD(BRNO,5,'0') BRNO, 
                  COUNT(DISTINCT APPLNO) AS APPNT_OPENACCSUCC_CNT
             FROM BIM${version_num}.DCM_BIM_NASEACCPTINFO_DTL_S
            WHERE pt_dt <= '${process_date}'
              AND pt_dt >= udf.firstDayOfMonth('${process_date}')
              AND CHANNELID IN ('4', '5', '6', '7', '8')
              AND STATUS in ('4')
              AND OPENEDACCNO <> ""
              AND TAKE_DATE = pt_dt
            GROUP BY ZONENO, BRNO) T5
         
             ON T0.ZONE_ID = T5.ZONENO
            AND T0.BRCH_ID = T5.BRNO
;


--Ƶ�ȣ�0-�գ�1-�£�2-����3-��
--ͳ�Ʊ�������ݿ���ҵ������(2-��)
INSERT INTO TABLE CBMS_MICRO_CONOPENACC_TMP1
  SELECT T0.ZONE_ID,
         T0.BRCH_ID,
         2,
         IF(T1.ENTPONE_APPNT_CNT IS NULL,0,T1.ENTPONE_APPNT_CNT),
         IF(T2.HDLED_BUS_LINSE IS NULL,0,T2.HDLED_BUS_LINSE),
         IF(T3.ENTPONE_OPENACC_CNT IS NULL,0,T3.ENTPONE_OPENACC_CNT),
         IF(T4.APPNT_OPENACC_CNT IS NULL,0,T4.APPNT_OPENACC_CNT),
         IF(T5.APPNT_OPENACCSUCC_CNT IS NULL,0,T5.APPNT_OPENACCSUCC_CNT)
    FROM (SELECT LPAD(ZONE_ID,5,'0') ZONE_ID, 
                 LPAD(BRCH_ID,5,'0') BRCH_ID,
                 STRU_ID
           FROM CBS${version_num}.DCM_CBS_EDW_MSC_BRANCH_S
          WHERE pt_dt = '${process_date}') T0
         
           LEFT OUTER JOIN
         
          (SELECT STRU_ID, 
                  COUNT(APPLY_NO) AS ENTPONE_APPNT_CNT
             FROM CBS${version_num}.DCM_CBS_MSC_QYT_BUSINESS_INF_S
            WHERE pt_dt <= '${process_date}'
              AND pt_dt >= udf.firstDayOfSeason('${process_date}')
              AND DATA_TYPE = '1'
              AND CREAT_DATE = regexp_replace(pt_dt,'-','')
            GROUP BY STRU_ID) T1
         
             ON T0.STRU_ID = T1.STRU_ID
         
           LEFT OUTER JOIN
         
          (SELECT STRU_ID, 
                  COUNT(DISTINCT APPLY_NO) AS HDLED_BUS_LINSE
             FROM CBS${version_num}.DCM_CBS_MSC_QYT_BUSINESS_INF_S
            WHERE pt_dt <= '${process_date}'
              AND pt_dt >= udf.firstDayOfSeason('${process_date}')
              AND DATA_TYPE = '1'
              AND STATUS IN ('2', '3', '4')
            GROUP BY STRU_ID) T2
         
             ON T0.STRU_ID = T2.STRU_ID
         
           LEFT OUTER JOIN
         
          (SELECT STRU_ID, 
                  COUNT(DISTINCT APPLY_NO) AS ENTPONE_OPENACC_CNT
             FROM CBS${version_num}.DCM_CBS_MSC_QYT_BUSINESS_INF_S
            WHERE pt_dt <= '${process_date}'
              AND pt_dt >= udf.firstDayOfSeason('${process_date}')
              AND DATA_TYPE = '1'
              AND STATUS = '4'
            GROUP BY STRU_ID) T3
         
             ON T0.STRU_ID = T3.STRU_ID
         
           LEFT OUTER JOIN
         
          (SELECT LPAD(ZONENO,5,'0') ZONENO, 
                  LPAD(BRNO,5,'0') BRNO,  
                  COUNT(APPLNO) AS APPNT_OPENACC_CNT
             FROM BIM${version_num}.DCM_BIM_NASEACCPTINFO_DTL_S
            WHERE CHANNELID IN ('4', '5', '6', '7', '8')
              AND REG_DATE <= '${process_date}'
              AND REG_DATE >= udf.firstDayOfSeason('${process_date}')
              AND pt_dt = '${process_date}'
            GROUP BY ZONENO, BRNO) T4
         
             ON T0.ZONE_ID = T4.ZONENO
            AND T0.BRCH_ID = T4.BRNO
         
           LEFT OUTER JOIN
         
          (SELECT LPAD(ZONENO,5,'0') ZONENO, 
                  LPAD(BRNO,5,'0') BRNO, 
                  COUNT(DISTINCT APPLNO) AS APPNT_OPENACCSUCC_CNT
             FROM BIM${version_num}.DCM_BIM_NASEACCPTINFO_DTL_S
            WHERE pt_dt <= '${process_date}'
              AND pt_dt >= udf.firstDayOfSeason('${process_date}')
              AND CHANNELID IN ('4', '5', '6', '7', '8')
              AND STATUS in ('4')
              AND OPENEDACCNO <> ""
              AND TAKE_DATE = pt_dt
            GROUP BY ZONENO, BRNO) T5
         
             ON T0.ZONE_ID = T5.ZONENO
            AND T0.BRCH_ID = T5.BRNO
;

--Ƶ�ȣ�0-�գ�1-�£�2-����3-��
--ͳ�Ʊ�������ݿ���ҵ������(3-��)
INSERT INTO TABLE CBMS_MICRO_CONOPENACC_TMP1
  SELECT T0.ZONE_ID,
         T0.BRCH_ID,
         3,
         IF(T1.ENTPONE_APPNT_CNT IS NULL,0,T1.ENTPONE_APPNT_CNT),
         IF(T2.HDLED_BUS_LINSE IS NULL,0,T2.HDLED_BUS_LINSE),
         IF(T3.ENTPONE_OPENACC_CNT IS NULL,0,T3.ENTPONE_OPENACC_CNT),
         IF(T4.APPNT_OPENACC_CNT IS NULL,0,T4.APPNT_OPENACC_CNT),
         IF(T5.APPNT_OPENACCSUCC_CNT IS NULL,0,T5.APPNT_OPENACCSUCC_CNT)
    FROM (SELECT LPAD(ZONE_ID,5,'0') ZONE_ID, 
                 LPAD(BRCH_ID,5,'0') BRCH_ID, 
                 STRU_ID
           FROM CBS${version_num}.DCM_CBS_EDW_MSC_BRANCH_S
          WHERE pt_dt = '${process_date}') T0
         
           LEFT OUTER JOIN
         
          (SELECT STRU_ID, 
                  COUNT(APPLY_NO) AS ENTPONE_APPNT_CNT
             FROM CBS${version_num}.DCM_CBS_MSC_QYT_BUSINESS_INF_S
            WHERE pt_dt <= '${process_date}'
              AND pt_dt >= udf.firstDayOfYear('${process_date}')
              AND DATA_TYPE = '1'
              AND CREAT_DATE = regexp_replace(pt_dt,'-','')
            GROUP BY STRU_ID) T1
         
             ON T0.STRU_ID = T1.STRU_ID
         
           LEFT OUTER JOIN
         
          (SELECT STRU_ID, 
                  COUNT(DISTINCT APPLY_NO) AS HDLED_BUS_LINSE
             FROM CBS${version_num}.DCM_CBS_MSC_QYT_BUSINESS_INF_S
            WHERE pt_dt <= '${process_date}'
              AND pt_dt >= udf.firstDayOfYear('${process_date}')
              AND DATA_TYPE = '1'
              AND STATUS IN ('2', '3', '4')
            GROUP BY STRU_ID) T2
         
             ON T0.STRU_ID = T2.STRU_ID
         
           LEFT OUTER JOIN
         
          (SELECT STRU_ID, 
                  COUNT(DISTINCT APPLY_NO) AS ENTPONE_OPENACC_CNT
             FROM CBS${version_num}.DCM_CBS_MSC_QYT_BUSINESS_INF_S
            WHERE pt_dt <= '${process_date}'
              AND pt_dt >= udf.firstDayOfYear('${process_date}')
              AND DATA_TYPE = '1'
              AND STATUS = '4'
            GROUP BY STRU_ID) T3
         
             ON T0.STRU_ID = T3.STRU_ID
         
           LEFT OUTER JOIN
         
          (SELECT LPAD(ZONENO,5,'0') ZONENO, 
                  LPAD(BRNO,5,'0') BRNO, 
                  COUNT(APPLNO) AS APPNT_OPENACC_CNT
             FROM BIM${version_num}.DCM_BIM_NASEACCPTINFO_DTL_S
            WHERE CHANNELID IN ('4', '5', '6', '7', '8')
              AND REG_DATE <= '${process_date}'
              AND REG_DATE >= udf.firstDayOfYear('${process_date}')
              AND pt_dt = '${process_date}'
            GROUP BY ZONENO, BRNO) T4
         
             ON T0.ZONE_ID = T4.ZONENO
            AND T0.BRCH_ID = T4.BRNO
         
           LEFT OUTER JOIN
         
          (SELECT LPAD(ZONENO,5,'0') ZONENO, 
                  LPAD(BRNO,5,'0') BRNO,
                  COUNT(DISTINCT APPLNO) AS APPNT_OPENACCSUCC_CNT
             FROM BIM${version_num}.DCM_BIM_NASEACCPTINFO_DTL_S
            WHERE pt_dt <= '${process_date}'
              AND pt_dt >= udf.firstDayOfYear('${process_date}')
              AND CHANNELID IN ('4', '5', '6', '7', '8')
              AND STATUS in ('4')
              AND OPENEDACCNO <> ""
              AND TAKE_DATE = pt_dt
            GROUP BY ZONENO, BRNO) T5
         
             ON T0.ZONE_ID = T5.ZONENO
            AND T0.BRCH_ID = T5.BRNO
;


--����������-1��һ����-2��������-3��ֱ����-9��֧��-4������-5
--Ƶ�ȣ�0-�գ�1-�£�2-����3-��
--ͳ�ƻ���Ͻ�ڱ�ݿ���ҵ������
--��ʱ���������
INSERT OVERWRITE TABLE BDSP_CBMS${version_num}.CBMS_MICRO_CONOPENACC_T partition(pt_dt='${process_date}')
SELECT  '${process_date}'
        ,T3.INT_FREQ
        ,T1.ZONEID_RECS
        ,T1.BRCHID_RECS
        ,T1.STRULEVEL_RECS
        ,SUM(T3.ENTPONE_APPNT_CNT)      AS ENTPONE_APPNT_CNT    
        ,SUM(T3.HDLED_BUS_LINSE)        AS HDLED_BUS_LINSE  
        ,SUM(T3.ENTPONE_OPENACC_CNT)    AS ENTPONE_OPENACC_CNT     
        ,SUM(T3.APPNT_OPENACC_CNT)    AS APPNT_OPENACC_CNT
        ,SUM(T3.APPNT_OPENACCSUCC_CNT)  AS APPNT_OPENACCSUCC_CNT 
FROM
(SELECT LPAD(ZONE_ID,5,'0') ZONE_ID, 
        LPAD(BRCH_ID,5,'0') BRCH_ID, 
        STRUNAME, 
        STRULEVEL, 
        ZONEID_RECS, 
        BRCHID_RECS, 
        STRUNAME_RECS, 
        STRULEVEL_RECS, 
        STRU_ID, 
        STRU_ID_RECS             
   FROM CBS${version_num}.DCM_CBS_MSC_C_BRCHRECS_S
  WHERE pt_dt = '${process_date}')T1

JOIN 

(SELECT  INT_ORG_ZONE
        ,INT_ORG_BRCH          
        ,INT_FREQ              
        ,ENTPONE_APPNT_CNT     
        ,HDLED_BUS_LINSE       
        ,ENTPONE_OPENACC_CNT   
        ,APPNT_OPENACC_CNT     
        ,APPNT_OPENACCSUCC_CNT   
   FROM CBMS_MICRO_CONOPENACC_TMP1) T3

ON T1.ZONE_ID = T3.INT_ORG_ZONE
AND T1.BRCH_ID = T3.INT_ORG_BRCH

GROUP BY  T1.ZONEID_RECS
         ,T1.BRCHID_RECS
         ,T1.STRULEVEL_RECS
         ,T3.INT_FREQ 
;

DROP TABLE IF EXISTS BDSP_CBMS${version_num}.CBMS_MICRO_CONOPENACC_TMP1;