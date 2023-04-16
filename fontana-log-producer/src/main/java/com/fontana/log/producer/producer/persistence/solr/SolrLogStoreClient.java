package com.fontana.log.producer.producer.persistence.solr;

import com.fontana.log.producer.producer.BaseLogContent;
import com.fontana.log.producer.producer.BaseLogItem;
import com.fontana.log.producer.producer.PutLogsRequest;
import com.fontana.log.producer.producer.PutLogsResponse;
import com.fontana.log.producer.producer.persistence.Constants;
import com.fontana.log.producer.producer.persistence.LogStoreClient;
import com.fontana.log.producer.producer.persistence.LogStoreConfig;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * @author yegenchang
 * @description
 * @date 2022/6/14 10:27
 */
public class SolrLogStoreClient implements LogStoreClient, Closeable {

  /**
   * solr客户端
   */
  private Http2SolrClient solrClient;

  private Logger logger = LoggerFactory.getLogger(SolrLogStoreClient.class);

  public SolrLogStoreClient (LogStoreConfig config){
    String host = config.config().get(Constants.LOG_STORE_CONFIG_KEY_SOLR_HOST);
    String logStore = config.config().get(Constants.LOG_STORE_NAME);
    if((host==null || host.isEmpty()) || (logStore==null || logStore.isEmpty())){
      throw new RuntimeException("host,logStore不能为空");
    }
    String url = MessageFormat.format("{0}/solr/{1}",host,logStore);
    solrClient = new Http2SolrClient.Builder(url).connectionTimeout(10000).build();
  }

  @Override
  public PutLogsResponse putLogs(PutLogsRequest request) {
    ArrayList<BaseLogItem> logItems = request.getLogItems();
    try {
      List<SolrInputDocument> documents = new ArrayList<>();
      for (int i = 0; i < logItems.size(); i++) {
        SolrInputDocument document = new SolrInputDocument();
        BaseLogItem item = logItems.get(i);
        Iterator var2 = item.mContents.iterator();
        while (var2.hasNext()) {
          BaseLogContent content = (BaseLogContent) var2.next();
          document.setField(content.getKey(), content.getValue());
        }
        documents.add(document);
      }
      solrClient.add(documents);
      solrClient.commit();
      logger.debug("日志投递完毕 count "+documents.size()+" 线程 ："+Thread.currentThread().getName());
    } catch (Exception e) {
      //防止日志服务器挂掉之后，一致重试，占用内存，这里忽略异常
      logger.debug("投递日志异常", e);
    }
    PutLogsResponse response = new PutLogsResponse();
    response.setRequestId(UUID.randomUUID().toString());
    return response;
  }

  @Override
  public void close() throws IOException {
    if(solrClient!=null) {
      try {
        solrClient.close();
      }catch (Exception e){
        throw new RuntimeException("Exception on closing client solrClient");
      }
    }
  }
}
