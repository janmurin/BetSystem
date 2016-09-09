/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package make_cleanDB;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Janco1
 */
public class RawDBSkupina {

    String nazovSkupiny;
    List<File> files=new ArrayList<>();

    RawDBSkupina(String skupina, List<File> filenames) {
        this.nazovSkupiny=skupina;
        this.files=filenames;
    }
    
}
