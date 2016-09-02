package services.bean;

public class ReadabilityResponse {
	
	private String excerpt;
    private String direction;
    private String next_page_id;
    private String lead_image_url;
    private String url;
    private String content;
    private String rendered_pages;
    private String author;
    private String title;
    private String short_url;
    private String word_count;
    private String domain;
    private String total_pages;
    private String dek;
    private String date_published;

    public String getExcerpt ()
    {
        return excerpt;
    }

    public void setExcerpt (String excerpt)
    {
        this.excerpt = excerpt;
    }

    public String getDirection ()
    {
        return direction;
    }

    public void setDirection (String direction)
    {
        this.direction = direction;
    }

    public String getNext_page_id ()
    {
        return next_page_id;
    }

    public void setNext_page_id (String next_page_id)
    {
        this.next_page_id = next_page_id;
    }

    public String getLead_image_url ()
    {
        return lead_image_url;
    }

    public void setLead_image_url (String lead_image_url)
    {
        this.lead_image_url = lead_image_url;
    }

    public String getUrl ()
    {
        return url;
    }

    public void setUrl (String url)
    {
        this.url = url;
    }

    public String getContent ()
    {
        return content;
    }

    public void setContent (String content)
    {
        this.content = content;
    }

    public String getRendered_pages ()
    {
        return rendered_pages;
    }

    public void setRendered_pages (String rendered_pages)
    {
        this.rendered_pages = rendered_pages;
    }

    public String getAuthor ()
    {
        return author;
    }

    public void setAuthor (String author)
    {
        this.author = author;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getShort_url ()
    {
        return short_url;
    }

    public void setShort_url (String short_url)
    {
        this.short_url = short_url;
    }

    public String getWord_count ()
    {
        return word_count;
    }

    public void setWord_count (String word_count)
    {
        this.word_count = word_count;
    }

    public String getDomain ()
    {
        return domain;
    }

    public void setDomain (String domain)
    {
        this.domain = domain;
    }

    public String getTotal_pages ()
    {
        return total_pages;
    }

    public void setTotal_pages (String total_pages)
    {
        this.total_pages = total_pages;
    }

    public String getDek ()
    {
        return dek;
    }

    public void setDek (String dek)
    {
        this.dek = dek;
    }

    public String getDate_published ()
    {
        return date_published;
    }

    public void setDate_published (String date_published)
    {
        this.date_published = date_published;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [excerpt = "+excerpt+", direction = "+direction+", next_page_id = "+next_page_id+", lead_image_url = "+lead_image_url+", url = "+url+", content = "+content+", rendered_pages = "+rendered_pages+", author = "+author+", title = "+title+", short_url = "+short_url+", word_count = "+word_count+", domain = "+domain+", total_pages = "+total_pages+", dek = "+dek+", date_published = "+date_published+"]";
    }
}
