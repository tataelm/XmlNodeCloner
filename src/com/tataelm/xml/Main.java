package com.tataelm.xml;

public class Main {

    public static void main(String[] args) {
        try {

            XmlNodeCloner xmlNodeCloner = new XmlNodeCloner("src/input.xml", "element", 2);
            //xmlNodeCloner.setIfCloneLatestOrCertain(true);
            //xmlNodeCloner.selectCloningElementByTagValue("name", "user12");
            xmlNodeCloner.addXmlTagsToEnumerate("name","count");
            xmlNodeCloner.isAlreadyIterated(true);
            xmlNodeCloner.setmEnumeratorStyle(XmlNodeCloner.EnumeratorStyle.DEFAULT);
            xmlNodeCloner.runCloner();


         /*   XmlNodeCloner xmlNodeCloner2 = new XmlNodeCloner("src/input2.xml", "note", 2);
            //xmlNodeCloner.setIfCloneLatestOrCertain(true);
            //xmlNodeCloner.selectCloningElementByTagValue("name", "user12");
            xmlNodeCloner2.addXmlTagsToEnumerate("heading");
            xmlNodeCloner2.setmEnumeratorStyle(XmlNodeCloner.EnumeratorStyle.UNDERSCORE);
            xmlNodeCloner2.runCloner();

            XmlNodeCloner xmlNodeCloner3 = new XmlNodeCloner("src/input3.xml", "book", 2);
            //xmlNodeCloner.setIfCloneLatestOrCertain(true);
            //xmlNodeCloner.selectCloningElementByTagValue("name", "user12");
            xmlNodeCloner3.addXmlTagsToEnumerate("genre");
            xmlNodeCloner3.setmEnumeratorStyle(XmlNodeCloner.EnumeratorStyle.UNDERSCORE);
            xmlNodeCloner3.runCloner();*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
