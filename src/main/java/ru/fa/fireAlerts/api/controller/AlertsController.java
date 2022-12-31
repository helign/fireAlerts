package ru.fa.fireAlerts.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.fa.fireAlerts.api.model.Alert;
import ru.fa.fireAlerts.api.model.Fire;
import ru.fa.fireAlerts.api.model.PointOfInterest;
import ru.fa.fireAlerts.api.model.request.PointOfInterestCreationRequest;
import ru.fa.fireAlerts.api.service.AlertsService;

@RestController
@RequestMapping(value = "/api/fires")
@RequiredArgsConstructor
public class AlertsController {
    private final AlertsService alertsService;

    @GetMapping("/{fireId}")
    public ResponseEntity<Fire> findFire(@PathVariable Long fireId){
        return
                ResponseEntity.ok(alertsService.findFire(fireId));
    }

    @GetMapping("/alert/{areaId}")
    public ResponseEntity<Alert> getAlert (@PathVariable Long areaId){
        return ResponseEntity.ok(alertsService.getAlert(areaId));
    }

    @PostMapping("/poi")
    public ResponseEntity<PointOfInterest> createPOI (@RequestBody PointOfInterestCreationRequest request){
        return
                ResponseEntity.ok(alertsService.createPOI(request));
    }

    @DeleteMapping("/poi/{poiID}")
    public ResponseEntity<Void> removePOI (@PathVariable Long poiID){
        alertsService.removePOI(poiID);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/poi/{poiID}")
    public ResponseEntity<PointOfInterest> updatePOI(@PathVariable Long poiID,
                                                     @RequestBody PointOfInterestCreationRequest request
                                                     ){
        return
                ResponseEntity.ok(alertsService.updatePOI(poiID,request));
    }
}
