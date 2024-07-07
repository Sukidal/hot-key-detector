package cn.edu.ecnu.stu.hotkeydetecter.server.controller;

import cn.edu.ecnu.stu.hotkeydetecter.server.data.PutReq;
import cn.edu.ecnu.stu.hotkeydetecter.server.store.StoreManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class StoreController {

    @Autowired
    public StoreManager storeManager;

    @GetMapping("/")
    public int[] read(@RequestParam("key") long key) {
        return storeManager.read(key);
    }

    @PostMapping("/")
    public void put(@RequestBody PutReq putReq) {
        storeManager.put(putReq.getKey(), putReq.getValue());
    }
}
