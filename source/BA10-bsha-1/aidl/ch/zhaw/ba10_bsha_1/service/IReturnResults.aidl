package ch.zhaw.ba10_bsha_1.service;


import ch.zhaw.ba10_bsha_1.Character;


/**
 * The callback interface used by the detection Service to send
 * synchronous notifications back to its clients. Note that this is a
 * one-way interface so the server does not block waiting for the client.
 *
 * Based on the remote service code sample from the API Demos example
 * of the Android SDK.
 *
 * @author Julian Hanhart, Dominik Giger
 */
oneway interface IReturnResults {

    /**
     * Return List of recognised Characters
     */
    void recognisedCharacters(in List<Character> characters);
    
    /**
     * Return recognised Character
     */
    void recognisedChar(char character, float probability);
}
