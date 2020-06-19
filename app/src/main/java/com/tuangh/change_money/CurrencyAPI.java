package com.tuangh.change_money;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public  class CurrencyAPI extends AsyncTask<String,String, String> {

    protected SingleTon arrayList;
   protected ProgressDialog pDialog;
    CurrencyListener currencyListener;
    Activity context;
    Spinner spinner1;
    Spinner spinner2;

    public CurrencyAPI(Activity context){
        this.context=context;
        arrayList=SingleTon.getInstance();
    }
    public CurrencyAPI(CurrencyListener currencyListener ,Activity context,Spinner spinner1,Spinner spinner2 ) {
        arrayList=SingleTon.getInstance();
        this.currencyListener=currencyListener;
        this.context=context;
        this.spinner1=spinner1;
        this.spinner2=spinner2;
    }

    public CurrencyAPI() {

    }

    @Override
    protected String doInBackground(String... urls) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        ArrayList<Currency> listCurrency = new ArrayList<Currency>();
        try {
            URL url = new URL(urls[0]);
            connection = (HttpURLConnection) url.openConnection();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            String data = buffer.toString();

            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

        @Override
    protected void onPostExecute(String data){
        super.onPostExecute(data);
        if (pDialog.isShowing())
            pDialog.dismiss();
        if (data == null)
            Toast.makeText(context, "Lỗi - Refresh lại", Toast.LENGTH_SHORT).show();
        //TODO
            Document document = null;
            try {
                document = (Document) DocumentBuilderFactory.newInstance().newDocumentBuilder()
                        .parse(new InputSource(new StringReader(data)));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }

            Element elementRSS = (Element) document.getElementsByTagName("rss").item(0);
//            NodeList nodeListRSS=document.getElementsByTagName("rss");//Lấy element có tag name là rss
//            Element elementRSS = (Element) nodeListRSS.item(0);//Lấy phần từ đầu tiên
            NodeList nodeListItem = elementRSS.getElementsByTagName("item");//Lấy danh sách các item
            SingleTon.currencies.clear();
            for (int i = 0; i < nodeListItem.getLength(); i++) {
                String s = nodeListItem.item(i).getChildNodes().item(0).getTextContent();
                String[] c = s.split("/");
                String codeCountry[] = c[1].split("\\(");
                String code = "" + codeCountry[1].charAt(0) + codeCountry[1].charAt(1) + codeCountry[1].charAt(2);
                String name = codeCountry[0];
                Currency currency = new Currency();
                currency.setNameCountry(name);
                currency.setCode(code);
                SingleTon.currencies.add(currency);
            }
            this.currencyListener.data(SingleTon.currencies);
            ArrayAdapter adapter=new ArrayAdapter(context,android.R.layout.simple_spinner_item,SingleTon.currencies);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner1.setAdapter(adapter);
            spinner2.setAdapter(adapter);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Proccesing..");
        pDialog.setCancelable(false);
        pDialog.show();
    }
}
