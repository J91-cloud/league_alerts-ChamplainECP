//package com.calerts.computer_alertsbe.utils.dataLoaders;
//
//
//import com.calerts.computer_alertsbe.readersubdomain.dataaccesslayer.AccountStatus;
//import com.calerts.computer_alertsbe.readersubdomain.dataaccesslayer.Reader;
//
//import com.calerts.computer_alertsbe.readersubdomain.dataaccesslayer.ReaderIdentifier;
//
//
//import com.calerts.computer_alertsbe.readersubdomain.dataaccesslayer.ReaderRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//@Service
//public class ReaderDataLoaderService implements CommandLineRunner {
//
//    @Autowired
//    ReaderRepository readerRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//        Reader reader1 = Reader.builder()
//
//                .readerIdentifier(new ReaderIdentifier())
//
////                .readerId("06a7d573-bcab-4db3-956f-773324b92a80")
//
//                .firstName("James")
//                .lastName("Jordan")
//                .address("2750 rue Bernard J4M34W")
//                .auth0userId("auth0|678920c84d650d4d4b64cee4")
//                .emailAddress("reader2@gmail.com")
//                .accountStatus(AccountStatus.ACTIVE)
//                .build();
//
//
//        Flux.just(reader1)
//                .flatMap(s -> readerRepository.insert(Mono.just(s))
//                        .log(s.toString()))
//                .subscribe();
//
//
//    }
//
//
//
//}
