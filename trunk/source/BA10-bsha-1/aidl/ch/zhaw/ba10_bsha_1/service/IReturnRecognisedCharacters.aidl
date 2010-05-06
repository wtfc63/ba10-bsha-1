package ch.zhaw.ba10_bsha_1.service;


import ch.zhaw.ba10_bsha_1.Character;


/**
 * Example of a callback interface used by IRemoteService to send
 * synchronous notifications back to its clients.  Note that this is a
 * one-way interface so the server does not block waiting for the client.
 */
oneway interface IReturnRecognisedCharacters {
    /**
     * Return recognised Characters
     */
    void recognisedCharacters(out List<Character> characters);
}
