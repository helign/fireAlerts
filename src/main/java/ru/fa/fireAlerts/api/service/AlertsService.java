package ru.fa.fireAlerts.api.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import ru.fa.fireAlerts.api.model.Alert;
import ru.fa.fireAlerts.api.model.AlertStatus;
import ru.fa.fireAlerts.api.model.Fire;
import ru.fa.fireAlerts.api.model.PointOfInterest;
import ru.fa.fireAlerts.api.model.request.PointOfInterestCreationRequest;
import ru.fa.fireAlerts.api.repository.AlertRepository;
import ru.fa.fireAlerts.api.repository.FireRepository;
import ru.fa.fireAlerts.api.repository.POIRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlertsService {

    private final FireRepository fireRepository;
    private final AlertRepository alertRepository;
    private final POIRepository poiRepository;

    public Fire findFire(Long id) {
        Optional<Fire> fire = fireRepository.findById(id);
        if (fire.isPresent()) {
            return fire.get();
        }
        throw new EntityNotFoundException("Cant find any fire under given ID");
    }

    public PointOfInterest createPOI(PointOfInterestCreationRequest request){
        PointOfInterest poi = new PointOfInterest();
        BeanUtils.copyProperties(request,poi);
        List<Fire> fireList = fireRepository.findAll().stream()
                .filter(poi::isFireInRadius).collect(Collectors.toList());
        poi.setFiresInRange(fireList);
        if (!fireList.isEmpty()){
            Alert newAlert = new Alert();
            newAlert.setArea(poi);
            newAlert.setStatus(AlertStatus.UNSENT);
            newAlert.setMessage("New fires in range: " + fireList.size());
            alertRepository.save(newAlert);
        }
        return poiRepository.save(poi);
    }

    public void removePOI(Long id){
        poiRepository.deleteById(id);
    }

    public PointOfInterest updatePOI(Long id, PointOfInterestCreationRequest request){
        Optional<PointOfInterest> optionalPOI = poiRepository.findById(id);
        if(!optionalPOI.isPresent()){
            throw new EntityNotFoundException("Point of interest not present in the database.");
        }
        PointOfInterest poi = optionalPOI.get();
        poi.setLongitude((request.getLongitude()));
        poi.setLatitude(request.getLatitude());
        poi.setRange(request.getRadius());
        List<Fire> fireList = fireRepository.findAll().stream()
                .filter(poi::isFireInRadius).collect(Collectors.toList());
        poi.setFiresInRange(fireList);
        return poiRepository.save(poi);
    }

    public Alert getAlert (Long area_id){
        Optional<PointOfInterest> optionalPoi = poiRepository.findById(area_id);
        if(!optionalPoi.isPresent()){
            throw new EntityNotFoundException("Point of interest not present in the database.");
        }
        PointOfInterest poi = optionalPoi.get();
        List<Fire> fireList = fireRepository.findAll().stream()
                .filter(poi::isFireInRadius).collect(Collectors.toList());
        poi.setFiresInRange(fireList);
        Alert newAlert = new Alert();
        if (!fireList.isEmpty()){
            newAlert.setArea(poi);
            newAlert.setStatus(AlertStatus.UNSENT);
            newAlert.setMessage("New fires in range: " + fireList.size());
            alertRepository.save(newAlert);
        }
        else {throw new EntityNotFoundException("No new alerts for the POI.");}
        return alertRepository.save(newAlert);
    }
}
