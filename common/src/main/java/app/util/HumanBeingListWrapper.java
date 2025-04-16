package app.util;

import app.model.HumanBeing;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper for storing HumanBeing list in XML
 */
@JacksonXmlRootElement(localName = "HumanBeingList")
public class HumanBeingListWrapper {

    // Jackson will serialize/deserialize the list of <HumanBeing> objects
    @JacksonXmlElementWrapper(useWrapping = false)
    public List<HumanBeing> HumanBeing = new ArrayList<>();

    public HumanBeingListWrapper() {
        // Empty constructor for Jackson
    }

    public HumanBeingListWrapper(List<HumanBeing> list) {
        this.HumanBeing = list;
    }
}
