import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
 
import java.io.IOException;
 
public class SolrJ {
 
 private static final String urlString = "http://localhost:8983/solr";
 private SolrServer solrServer;
 
 public SolrJ() {
  if (solrServer == null) {
   solrServer = new HttpSolrServer(urlString);
  }
 }
 
 public void deleteByQuery(String queryString) {
  try {
   solrServer.deleteByQuery(queryString);
  } catch (Exception e) {
   e.printStackTrace();
  }
 }
 
 public void addDocumentTest() {
 
  SolrInputDocument doc = new SolrInputDocument();
  doc.addField("id", "tsetstst3r4", 1.0f);
  doc.addField( "name", "doc1", 1.0f );
  doc.addField( "price", 10 );
  try {
   solrServer.add(doc);
  } catch (SolrServerException e) {
   e.printStackTrace();
  } catch (IOException e) {
   e.printStackTrace();
  }
 
 }
 
 public QueryResponse getRueryResponse(String queryString) {
  SolrQuery query = new SolrQuery();
  query.setQuery(queryString);
 
  QueryResponse queryResponse = null;
  try {
   queryResponse = solrServer.query(query);
  } catch (SolrServerException e) {
   e.printStackTrace();
  }
  return queryResponse;
 }
 
 public static void main(String[] args) {
  //Go to http://wiki.apache.org/solr/Solrj to look up various other SolrJ APIs 
   
  SolrJ solrJ = new SolrJ();
//  solrJ.addDocumentTest();  
  QueryResponse response = solrJ.getRueryResponse("q=video&rows=1&fl=id,name&wt=json");
  
  //q=video&rows=1&fl=id%2Cname&wt=json
  System.out.println("SolrJ 61 response =  " + response);
 }
}