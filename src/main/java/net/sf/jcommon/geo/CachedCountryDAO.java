package net.sf.jcommon.geo;

import java.util.*;

public class CachedCountryDAO extends AbstractCountryDAO {

	private CountryDAO decoratedDAO;
	
    /** The countries list. */
    private Collection<Country> allCountries = null;
    /** The countries list having an ISO code assigned. */
    private Collection<Country> isoCountries = null;
    /** ISO 2 country. */
    private Map<String, Country> iso2country = null;
    /** Name 2 country */
    private Map<String, Country> name2country = null;
    /** Language 2 country */
    private Map<String, Country> lang2country = null;
    /** Countries mapped by region */
    private Map<String, Collection<Country>> region2Countries; 

    public CachedCountryDAO(CountryDAO decoratedDAO) {
		this.decoratedDAO = decoratedDAO;
	}

	private void initCountries() {
    	Collection<Country> countries = decoratedDAO.getAllCountries();
		
		if (countries == null) {
			countries = new HashSet<Country>();
		}
    	
        allCountries = new TreeSet<Country>(new Country.NameComparator());
        isoCountries = new TreeSet<Country>(new Country.ISO2Comparator());
        iso2country = new HashMap<String, Country>(countries.size());
        name2country = new HashMap<String, Country>(countries.size());
        lang2country = new HashMap<String, Country>(countries.size());
        region2Countries = new HashMap<String, Collection<Country>>(countries.size());
        
        for (Country country : countries) {
	        allCountries.add(country);
	        if ((country.getISO() != null) && (country.getISO().length() > 0)) {
	            isoCountries.add(country);
	            iso2country.put(country.getISO(), country);
	            
	        }
	        name2country.put(country.getName(), country);
	        if (country.getDefaultForLanguages() != null && country.getDefaultForLanguages().length > 0) {
	        	String[] lls = country.getDefaultForLanguages();
	        	for (String ll : lls) {
	        		lang2country.put(ll, country);
	        	}
	        }
	        String r = country.getRegion();
	        if (r != null && r.length() > 0) {
	        	Collection<Country> cs = region2Countries.get(r);
	        	if (cs == null) {
	        		cs = new TreeSet<Country>(new Country.NameComparator());
	        		region2Countries.put(r, cs);
	        	}
	        	cs.add(country);
	        }
        }
    }
    
    @Override
	public Collection<Country> getISOCountries() {
        if (isoCountries == null) {
            initCountries();
        }
        return isoCountries;
    }

    @Override
	public Collection<Country> getAllCountries() {
        if (allCountries == null) {
        	initCountries();
        }
        return allCountries;
    }
    
    @Override
	public Collection<Country> getCountriesForRegion(String region) {
    	if (region == null) {
    		return null;
    	}
        if (region2Countries == null) {
        	initCountries();
        }
    	return region2Countries.get(region.toLowerCase());
    }

    @Override
	public Country findByISO2(String code) {
        if (code == null || code.length() != 2)
            return null;
        if (iso2country == null) {
        	initCountries();
        }
        return iso2country.get(code.toUpperCase());
    }
    
    @Override
	public Country findByLanguage(String code) {
        if (code == null || code.length() != 2)
            return null;
        if (lang2country == null) {
        	initCountries();
        }
        return lang2country.get(code.toUpperCase());
    }
    
    @Override
	public Country findByName(String name) {
    	if (name == null) {
    		return null;
    	}
        if (name2country == null) {
        	initCountries();
        }
        return name2country.get(name);
    }

}
