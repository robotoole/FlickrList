package com.robotoole.flickrlist.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;

/**
 * Picture model defined by Flickr. Annotated to support ORMLite.
 * Created by robert on 2/19/15.
 */
@DatabaseTable(tableName = "pictures")
public class Picture {
    public Picture() {
        // ORMLite needs a no-arg constructor
    }

    @DatabaseField(generatedId = true)
    int id;
    @DatabaseField
    private String title;
    @DatabaseField
    private String link;
    private String media;
    private String date_taken;
    private String description;
    private String published;
    private String author;
    private String author_id;
    private String tags;

    private List<String> tagsList;

//    <entry>
//    <title>rockDoveSQ</title>
//    <link rel="alternate" type="text/html" href="http://www.flickr.com/photos/projectvenus/16584746002/"/>
//    <id>tag:flickr.com,2005:/photo/16584746002</id>
//    <published>2015-02-20T01:14:58Z</published>
//    <updated>2015-02-20T01:14:58Z</updated>
//    <flickr:date_taken>2014-10-09T06:38:28-08:00</flickr:date_taken>
//    <dc:date.Taken>2014-10-09T06:38:28-08:00</dc:date.Taken>
//    <content type="html">			&lt;p&gt;&lt;a href=&quot;http://www.flickr.com/people/projectvenus/&quot;&gt;• Doug Cook •&lt;/a&gt; posted a photo:&lt;/p&gt;
//
//            &lt;p&gt;&lt;a href=&quot;http://www.flickr.com/photos/projectvenus/16584746002/&quot; title=&quot;rockDoveSQ&quot;&gt;&lt;img src=&quot;http://farm8.staticflickr.com/7329/16584746002_6c600baab2_m.jpg&quot; width=&quot;235&quot; height=&quot;240&quot; alt=&quot;rockDoveSQ&quot; /&gt;&lt;/a&gt;&lt;/p&gt;
//
//            &lt;p&gt;&lt;i&gt;Pigeon [rockDove] Painting&lt;/i&gt;&lt;br /&gt;
//    Government Center MBTA | &lt;i&gt;City Hall Plaza&lt;/i&gt; &lt;b&gt;Boston&lt;/b&gt;&lt;br /&gt;
//    &lt;br /&gt;
//    &lt;i&gt;qwikLoadr&lt;/i&gt;™ Videos...&lt;br /&gt;
//    &lt;a href=&quot;http://player.vimeo.com/video/29553867&quot; rel=&quot;nofollow&quot;&gt;&lt;b&gt;Finger Eleven&lt;/b&gt; | Paralyzer&lt;/a&gt; &lt;i&gt;Official&lt;/i&gt; • Vimeo™&lt;br /&gt;
//            &lt;a href=&quot;http://player.vimeo.com/video/28323439&quot; rel=&quot;nofollow&quot;&gt;&lt;b&gt;Red Hot Chili Peppers&lt;/b&gt; | Raindance Maggie&lt;/a&gt; &lt;i&gt;Venice Beach&lt;/i&gt; • Vimeo™&lt;br /&gt;
//            &lt;br /&gt;
//    &lt;i&gt;Blogger GrfxDziner&lt;/i&gt; | &lt;b&gt;the Joshua Tree...&lt;/b&gt;&lt;br /&gt;
//    &lt;a href=&quot;http://GrfxDziner.blogspot.com/2009/09/joshua-tree-from-gwennie2006.html&quot; rel=&quot;nofollow&quot;&gt;GrfxDziner.blogspot.com/2009/09/joshua-tree-from-gwennie2...&lt;/a&gt;&lt;br /&gt;
//            &lt;br /&gt;
//    Edited in PicMonkey, Square crop &amp;amp; Film Frame.&lt;/p&gt;</content>
//    <author>
//    <name>• Doug Cook •</name>
//    <uri>http://www.flickr.com/people/projectvenus/</uri>
//    <flickr:nsid>56073911@N08</flickr:nsid>
//    <flickr:buddyicon>http://farm2.staticflickr.com/1440/buddyicons/56073911@N08.jpg?1290001787#56073911@N08</flickr:buddyicon>
//    </author>
//    <link rel="enclosure" type="image/jpeg" href="http://farm8.staticflickr.com/7329/16584746002_6c600baab2_b.jpg" />
//    <category term="boston" scheme="http://www.flickr.com/photos/tags/" />
//    <category term="dc" scheme="http://www.flickr.com/photos/tags/" />
//    <category term="bostonma" scheme="http://www.flickr.com/photos/tags/" />
//    <category term="squarecrop" scheme="http://www.flickr.com/photos/tags/" />
//    <category term="rockdove" scheme="http://www.flickr.com/photos/tags/" />
//    <category term="filmframe" scheme="http://www.flickr.com/photos/tags/" />
//    <category term="dcmemorialfoundation" scheme="http://www.flickr.com/photos/tags/" />
//    <category term="picmonkey" scheme="http://www.flickr.com/photos/tags/" />
//    <displaycategories>
//    </displaycategories>
//    </entry>

    /**
     * Parse the XML node into a strong typed object.
     * Sorry, this is ugly but XML made this a lot uglier.
     *
     * @param xmlNode
     * @return
     */
    public Picture copyFrom(Node xmlNode) {
        Element fstElmnt = (Element) xmlNode;
        //set title
        NodeList nameList = fstElmnt.getElementsByTagName("title");
        Element nameElement = (Element) nameList.item(0);
        nameList = nameElement.getChildNodes();
        setTitle(((Node) nameList.item(0)).getNodeValue());


        //set image url
        NodeList linksList = fstElmnt.getElementsByTagName("link");
        Element linkElement = null;
        for (int i = 0; i < linksList.getLength(); i++) {
            linkElement = (Element) linksList.item(i);
            if (linkElement.getAttribute("type").equalsIgnoreCase("image/jpeg")) {
                break;
            }
        }
        setLink(linkElement.getAttribute("href"));

        return this;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getDate_taken() {
        return date_taken;
    }

    public void setDate_taken(String date_taken) {
        this.date_taken = date_taken;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_dd) {
        this.author_id = author_dd;
    }

    public List<String> getTagsList() {
        return tagsList;
    }

    public void setTagsList(List<String> tags) {
        this.tagsList = tags;
    }

}
