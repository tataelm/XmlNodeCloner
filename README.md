# Xml Node Cloner

This tool is a helper tool to clone and enumerate XML Nodes. It gives a configurable interface

[DOWNLOAD JAR FILE](https://github.com/tataelm/XmlNodeCloner/raw/master/out/artifacts/XmlNodeCloner_jar/XmlNodeCloner.jar)

## How to use it

Let's say we have this XML file. We want to clone <element> and enumerate <name> and <count>
```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?><rpc>
    <edit-config>
        <config>
            <element>
                <name>user1</name>
                <place>wakanda</place>
                <count>number1</count>
            </element>
            <element>
                <name>user2</name>
                <place>wakanda</place>
                <count>number2</count>
            </element>
            <element>
                <name>user3</name>
                <place>wakanda</place>
                <count>number3</count>
            </element>
		</config>
    </edit-config>
</rpc>
```

We call XmlNodeCloner with 3 parameters

```java
//Initiliaze the XmlNodeCloner with 3 parameters
// @param xmlPath     path to XML File to configure
// @param elementName which element suppose to be cloned
// @param cloneCount  how many iteration of that element will be produced 
XmlNodeCloner xmlNodeCloner = new XmlNodeCloner("src/input.xml", "element", 2);
xmlNodeCloner.addXmlTagsToEnumerate("name","count");
xmlNodeCloner.runCloner();
```

If you don't want to enumerate, you simply don't call the ***xmlNodeCloner.addXmlTagsToEnumerate("name","count");*** line


If the XML input already enumerated, we can set it like this. This way, it will check the end regex of string and continue from there
```java
xmlNodeCloner.isAlreadyIterated(true);
```

There are 5 different enumeration styles. If you don't assign anything, it is by default DEFAULT style
  - [DEFAULT] valuenumber
  - [DASH] value-number
  - [DASH_SPACE_DASH] value - number
  - [UNDERSCORE] value_number
  - [SPACE_UNDERSCORE_SPACE] value _ number
   
```java
xmlNodeCloner.setmEnumeratorStyle(EnumeratorStyle.DEFAULT);
```

By default, the cloning starts by the end index. Start cloning from a certain index would be like this:
```java
xmlNodeCloner.setIfCloneLatestOrCertain(true);
xmlNodeCloner.selectCloningElementByTagValue("name", "user2");
```

## Knows issues
  - There is no configuration to work with attributes



### NOTES
  - Used Java 1.8 without any external library
  - Used IDE: IntelliJ community

