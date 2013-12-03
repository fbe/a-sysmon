package com.ajjpj.asysmon.server;


/**
 * @author arno
 */
public class Components { // implements AShutdownable {
//    private static volatile Components INSTANCE;
//
//    private final InputProcessor inputProcessor;
//    private final SystemClockCorrector systemClockCorrector;
//
//    private final EventBus eventBus;
//
//    private final DataPersister dataPersister;
//    private final DataProvider dataProvider;
//    private final BufferingPersistenceProcessorImpl persistenceProcessor;
//
//
//    public static void init(ASysMonServerConfig config) throws Exception {
//        if(INSTANCE != null) {
//            throw new IllegalStateException("already initialized");
//        }
//
//        final EventBus eventBus = new EventBusImpl();
//        final SystemClockCorrector clockCorrector = new SystemClockCorrectorNullImpl(); //TODO make clock corrector configurbale
//        final InputProcessor inputProc = new InputProcessorImpl(clockCorrector, eventBus);
//
//        final DataStoreImpl dataStore = new DataStoreImpl();
//        final BufferingPersistenceProcessorImpl persistenceProcessor = new BufferingPersistenceProcessorImpl(eventBus, dataStore, config);
//
//        INSTANCE = new Components(inputProc, clockCorrector, eventBus, dataStore, dataStore, persistenceProcessor);
//
////        System.out.println(dataStore.findAll("demo", "load-1-minute", 0, Long.MAX_VALUE, "the-instance"));
//    }
//
//    private Components(InputProcessor inputProcessor, SystemClockCorrector systemClockCorrector, EventBus eventBus,
//                       DataPersister dataPersister, DataProvider dataProvider, BufferingPersistenceProcessorImpl persistenceProcessor) {
//        this.inputProcessor = inputProcessor;
//        this.systemClockCorrector = systemClockCorrector;
//        this.eventBus = eventBus;
//
//        this.dataPersister = dataPersister;
//        this.dataProvider = dataProvider;
//        this.persistenceProcessor = persistenceProcessor;
//    }
//
//    public static Components get() {
//        return INSTANCE;
//    }
//
//    public InputProcessor getInputProcessor() {
//        return inputProcessor;
//    }
//    public SystemClockCorrector getSystemClockCorrector() {
//        return systemClockCorrector;
//    }
//    public EventBus getEventBus() {
//        return eventBus;
//    }
//
//
//    @Override public void shutdown() throws Exception { //TODO who calls this?
//        persistenceProcessor.shutdown();
//    }
}

