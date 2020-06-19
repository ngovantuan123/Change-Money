package com.tuangh.change_money;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

class ConvertCurrencyAPI extends AsyncTask<String,String,String> {
        CurrencyListener listener;
        Activity context;


    public ConvertCurrencyAPI(CurrencyListener listener,Activity context) {
            super();
            this.listener=listener;
            this.context=context;
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
    protected void onPreExecute() {
        super.onPreExecute();
//        pDialog = new ProgressDialog(context);
//        pDialog.setMessage("Proccesing..");
//        pDialog.setCancelable(false);
//        pDialog.show();

    }

    @Override
        protected void onPostExecute(String data){
//        if (pDialog.isShowing())
//            pDialog.dismiss();
//        if (data == null)
//            Toast.makeText(context, "Lỗi - Refresh lại", Toast.LENGTH_SHORT).show();
            if(data==null) data="";
            try {
                Document document = (Document) DocumentBuilderFactory.newInstance().newDocumentBuilder()
                        .parse(new InputSource(new StringReader(data)));
                Element elementRSS = (Element) document.getElementsByTagName("rss").item(0);
                Element elementItem = (Element) elementRSS.getElementsByTagName("item").item(0);
                Element elementDes = (Element) elementItem.getElementsByTagName("description").item(0);

                String stringConvert[]=elementDes.getTextContent().split("<br/>");
                String currencyConvert[] = stringConvert[0].split("=");
                listener.setTyGia(tyGiaProcess(currencyConvert[1]));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        }

        private Double tyGiaProcess(String s) {
            String tyGia="";
            for(int i=0;i<s.length()-3;i++){
                tyGia+=s.charAt(i);
            }
            return Double.parseDouble(tyGia);
        }
    }
