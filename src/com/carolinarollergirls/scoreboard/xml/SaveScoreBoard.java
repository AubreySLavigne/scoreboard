package com.carolinarollergirls.scoreboard.xml;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import org.jdom.*;
import org.jdom.output.*;

import com.carolinarollergirls.scoreboard.*;

public class SaveScoreBoard extends SegmentedXmlDocumentManager
{
  public SaveScoreBoard() { super("SaveLoad", "Save"); }

  public void setXmlScoreBoard(XmlScoreBoard xsB) {
    super.setXmlScoreBoard(xsB);

    Element e = createXPathElement();
    e.addContent(new Element("Filename"));
    e.addContent(new Element("Save"));
    e.addContent(editor.setText(new Element("Error"), "false"));
    e.addContent(new Element("Message"));
    update(e);
  }

  public void reset() {
    /* Don't reset anything, as these controls should not be saved. */
  }

  protected void processChildElement(Element e) {
    if (e.getName() == "Filename")
      update(editor.cloneDocumentToElement(e, true));
    else if (e.getName() == "Save")
      save();
  }

  protected void save() {
    Element msg = new Element("Message");
    Element error = editor.setText(new Element("Error"), "false");
    Element updateE = createXPathElement().addContent(msg).addContent(error);
    String filename = "";
    try {
      filename = editor.getText(getXPathElement().getChild("Filename"));
      FileOutputStream fos = new FileOutputStream(new File(DIRECTORY_NAME, filename));
      xmlOutputter.output(editor.filterNoSavePI(getXmlScoreBoard().getDocument()), fos);
      fos.close();
      editor.setText(msg, "Saved ScoreBoard to file '"+filename+"'");
    } catch ( Exception e ) {
      editor.setText(msg, "Could not save to file '"+filename+"' : "+e.getMessage());
      editor.setText(error, "true");
    } finally {
      update(updateE);
    }
  }

  protected Element createXPathElement() {
    return editor.setNoSavePI(super.createXPathElement());
  }

  protected XMLOutputter xmlOutputter = XmlDocumentEditor.getPrettyXmlOutputter();

  public static final String DIRECTORY_NAME = "html/save";

}
