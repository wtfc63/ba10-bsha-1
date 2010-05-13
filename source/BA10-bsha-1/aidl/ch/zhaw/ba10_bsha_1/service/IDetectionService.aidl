package ch.zhaw.ba10_bsha_1.service;


import ch.zhaw.ba10_bsha_1.service.IReturnResults;
import ch.zhaw.ba10_bsha_1.TouchPoint;
import ch.zhaw.ba10_bsha_1.StrategyArgument;


/**
 * Example of defining an interface for calling on to a remote service
 * (running in another process).
 */
interface IDetectionService {

    /**
     * Registering callback interface with service.
     */
    void registerCallback(IReturnResults cb);
    
    /**
     * Remove a previously registered callback interface.
     */
    void unregisterCallback(IReturnResults cb);
    
    
    /**
     * Add TouchPoints to detection queue
     */
    void addTouchPoints(in List<TouchPoint> points, boolean start_detection);
    
    /**
     * Add a TouchPoint to detection queue
     */
    void addTouchPoint(float pos_x, float pos_y, float strength, long timestamp);
    
    /**
     * End sample (Touch-up)
     */
    void endSample();
    
    
    /**
     * Get List of all available strategies (separated by semicolon: "type;name;description,enabled")
     */
    List<String> getAvailableStrategies();
    
    /**
     * Get List of all actively used strategies (separated by semicolon: "type;name;description")
     */
    List<String> getActiveStrategies();
    
    /**
     * Get a listing of the arguments of a strategy
     */
    List<StrategyArgument> getStrategyConfiguration(String strategy_name, int type);
    
    /**
     * Set an argument in all strategies supporting the given argument
     */
    void broadcastArgument(in StrategyArgument argument);
    
    /**
     * Set an argument of a strategy
     */
    void setStrategyArgument(in StrategyArgument argument, int type);
}
