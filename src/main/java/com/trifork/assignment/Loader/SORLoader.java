package com.trifork.assignment.Loader;

import com.trifork.assignment.Model.OrgDTO;

/**
 * The SORLoader interface is responsible for loading organizations from a
 * source, such as a CSV file, or a database, for initial creation.
 */
public interface SORLoader {
    public OrgDTO[] getAll();
}
