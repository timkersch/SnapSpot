package edit.com.backend.records;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.repackaged.com.google.api.client.util.DateTime;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;

/**
 * Created by: Tim Kerschbaumer
 * Project: SnapSpot
 * Date: 15-05-15
 * Time: 14:19
 */
@Entity
public class SpotRecord {

	@Id
	Long id;

	@Index
	private String name;
	private GeoPt geoPt;
	private String description;
	private Date date;

	public GeoPt getGeoPt() {
		return geoPt;
	}

	public void setGeoPt(GeoPt geoPt) {
		this.geoPt = geoPt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}


}
