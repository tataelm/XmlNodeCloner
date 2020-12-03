package com.tataelm.xml;

import java.io.File;
import java.util.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class XmlNodeCloner {

    private final String mXmlPath;
    private final String mElement;
    private final int mCloneCount;
    private final List<String> mXmlTagsToEnumerate = new ArrayList<>();

    private Document document;

    private boolean mIsLatest = true;
    private String mCloneStartElement = null;
    private String mCloneStartValue = null;

    private boolean mIsAlreadyIterated = false;
    private int mLastIterationNumber = 0;
    private boolean mIsLastIterationIndexChecked = false;

    private EnumeratorStyle mEnumeratorStyle = EnumeratorStyle.DEFAULT;

    /**
     * @param xmlPath     path to XML File to configure
     * @param elementName which element suppose to be cloned
     * @param cloneCount  how many iteration of that element will be produced *
     */
    public XmlNodeCloner(String xmlPath, String elementName, int cloneCount) {
        mXmlPath = xmlPath;
        mElement = elementName;
        mCloneCount = cloneCount;
    }

    /**
     * @param tags tags will be enumerated during the duplication
     **/
    public void addXmlTagsToEnumerate(String... tags) {
        Collections.addAll(mXmlTagsToEnumerate, tags);
    }

    /**
     * By default, it clones always the latest.
     *
     * @param certain if true, selectCloningElementByTagValue() method must be
     *                called and given parameters
     */
    public void setIfCloneLatestOrCertain(boolean certain) {
        mIsLatest = !certain;
    }


    /**
     * Checks if already an iteration has been done, so it can take the last number
     * from previous iteration
     * */
    public void isAlreadyIterated(boolean isAlreadyIterated) {
        this.mIsAlreadyIterated = isAlreadyIterated;
    }

    public void setmEnumeratorStyle(EnumeratorStyle mEnumeratorStyle) {
        this.mEnumeratorStyle = mEnumeratorStyle;
    }

    /**
     * @param element which node should be selected
     * @param value is that node's value
     */
    public void selectCloningElementByTagValue(String element, String value) {
        mCloneStartElement = element;
        mCloneStartValue = value;
    }

    public void runCloner() throws Exception {

        if (!mIsLatest && mCloneStartElement == null)
            throw new Exception(
                    "You must select an element to clone. Please call the method selectCloningElementByTagValue()"
                            + " or setIfCloneLatestOrCertain(false)");

        handleXmlFile();
    }

    private void handleXmlFile() {
        try {
            openXmlFile();

            // Get all the nodes
            NodeList nodeList = document.getElementsByTagName(mElement);
            Node choosenNode;

            // Get the chosen node, or by default the latest node
            if (mIsLatest) {
                choosenNode = getLatestNode(nodeList);
            } else {
                choosenNode = getCertainNode(nodeList);
            }

            // This is the root node, where clone nodes suppose to be added
            assert choosenNode != null;
            Node root = choosenNode.getParentNode();
            // Check if choosenNode already root node
            if(root.getParentNode() == null)
                root = choosenNode;


            // This contains all the nodes we need to work with
            Map<String, XmlValueHolder> hashmapChildNodes = fillHashMapChildNodes(choosenNode);

            // Clone the nodes

            for (int cloneIndex = 1; cloneIndex <= mCloneCount; cloneIndex++) {
                Element mainElement = document.createElement(mElement);

                for (String nodeName : hashmapChildNodes.keySet()) {

                    Element childElement = document.createElement(nodeName);
                    String textValue;

                    if (hashmapChildNodes.get(nodeName).toEnumerate) {
                        textValue = startEnumeration(hashmapChildNodes.get(nodeName).value, cloneIndex);
                        // textValue = hashmapChildNodes.get(nodeName).value + cloneIndex;
                    } else {
                        textValue = hashmapChildNodes.get(nodeName).value;
                    }

                    childElement.appendChild(document.createTextNode(textValue));
                    mainElement.appendChild(childElement);
                }

                root.appendChild(mainElement);
            }

            DOMSource source = new DOMSource(document);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(mXmlPath);
            transformer.transform(source, result);

        } catch (Exception ex) {
            System.out.println("Exception during duplication. Message: " + ex.getMessage());
        }
    }

    /**
     * Set the style of enumeration with dash, underscore etc..
     * */
    private String startEnumeration(String value, int cloneIndex) {

        if(mIsAlreadyIterated)
        {
            cloneIndex = cloneIndex + mLastIterationNumber;
        }


        switch (mEnumeratorStyle) {
            default:
            case DEFAULT:
                return value + cloneIndex;

            case DASH:
                return value + "-" + cloneIndex;


            case SPACE_DASH_SPACE:
                return value + " - " + cloneIndex;

            case UNDERSCORE:
                return value + "_" + cloneIndex;

            case SPACE_UNDERSCORE_SPACE:
                return value + " _ " + cloneIndex;
        }
    }



    /**
     * This fills hashmapChildNodes with nodes and their XmlValueHolders, which
     * holds the information of whether they get enumerated or not
     */
    private Map<String, XmlNodeCloner.XmlValueHolder> fillHashMapChildNodes(Node choosenNode) {
        Map<String, XmlValueHolder> hashmapChildNodes = new HashMap<>();

        NodeList nodeListForChildElements = choosenNode.getChildNodes();
        for (int i = 0; i < nodeListForChildElements.getLength(); i++) {
            Node currentNode = nodeListForChildElements.item(i);

            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                boolean isToEnumerate;

                if (!hashmapChildNodes.containsKey(currentNode.getNodeName())) {
                    if (mXmlTagsToEnumerate.contains(currentNode.getNodeName())) {

                        isToEnumerate = true;

                        if(mIsAlreadyIterated)
                            findLastIterationIndex(currentNode.getTextContent());

                    } else {
                        isToEnumerate = false;
                    }

                    // If it is already iterated, remove the numbers at the end
                    if(mIsAlreadyIterated)
                    {
                        int digits = String.valueOf(mLastIterationNumber).length();
                        String value = currentNode.getTextContent().substring(0,
                                currentNode.getTextContent().length() - digits);

                        hashmapChildNodes.put(currentNode.getNodeName(),
                                new XmlValueHolder(value, isToEnumerate));
                    }
                    else
                    {
                        hashmapChildNodes.put(currentNode.getNodeName(),
                                new XmlValueHolder(currentNode.getTextContent(), isToEnumerate));
                    }

                }
            }
        }
        return hashmapChildNodes;
    }

    /**
     * Find the last iteration index by looking end of the string value
     * */
    private void findLastIterationIndex(String textContent) {

        if(!mIsLastIterationIndexChecked)
        {
            try
            {
                mLastIterationNumber = Integer.parseInt(textContent.replaceFirst("^.*\\D",""));
                mIsLastIterationIndexChecked = true;
            }
            catch (Exception e) {
                mLastIterationNumber = 0;
            }
        }
    }

    /**
     * Opens the XML file and assigns it to 'document'
     */
    private void openXmlFile() {
        try {
            File file = new File(mXmlPath);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(file);
            document.getDocumentElement().normalize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the selected node from XML
     */
    private Node getCertainNode(NodeList nodeList) {

        for (int index = nodeList.getLength() - 1; index >= 0; index--) {
            Element element;
            Node node = nodeList.item(index);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                element = (Element) node;
                if (element.getElementsByTagName(mCloneStartElement).item(0).getTextContent()
                        .equals(mCloneStartValue)) {
                    return node;
                }
            }
        }
        return null;
    }

    /**
     * @return the latest node from XML
     */
    private Node getLatestNode(NodeList nodeList) {

        for (int index = nodeList.getLength() - 1; index >= 0; index--) {

            Node node = nodeList.item(index);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                return node;
            }
        }
        return null;
    }

    /**
     * Helper class for hashmapChildNodes
     */
    private static class XmlValueHolder {
        String value;
        boolean toEnumerate;

        public XmlValueHolder(String value, boolean toEnumerate) {
            this.value = value;
            this.toEnumerate = toEnumerate;
        }
    }


}
