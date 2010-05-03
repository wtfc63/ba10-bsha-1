package ch.zhaw.ba10_bsha_1.service;


import ch.zhaw.ba10_bsha_1.service.IReturnRecognisedCharacters;
import ch.zhaw.ba10_bsha_1.TouchPoint;


/**
 * Example of defining an interface for calling on to a remote service
 * (running in another process).
 */
interface IDetectionService {
    /**
     * Registering callback interface with service.
     */
    void registerCallback(IReturnRecognisedCharacters cb);
    
    /**
     * Remove a previously registered callback interface.
     */
    void unregisterCallback(IReturnRecognisedCharacters cb);
    
    /**
     * Add TouchPoints to detection queue
     */
    void addTouchPoints(in List<TouchPoint> points);
}
