package com.softcomputer.genetrace.tracessearchwebapp.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.softcomputer.genetrace.Data;
import com.softcomputer.genetrace.ReadFromFile;
import com.softcomputer.genetrace.tracessearchwebapp.dao.SettingsRepository;
import com.softcomputer.genetrace.tracessearchwebapp.model.Settings;
import com.softcomputer.genetrace.tracessearchwebapp.validator.Validator;

@Controller
@Scope("session")
public class HomeController {

    @Autowired
    ReadFromFile readFromFile;


    @Autowired
    SettingsRepository settingsRepository;


    List<String> paths = new ArrayList<String>();
    String dateStartGlobal;
    String dateEndGlobal;
    Boolean splitGlobal;
    @Value("${pathoffset}")
    String pathoffset;

    @Value("${linesBefore}")
    int linesBefore;

    @Value("${linesAfter}")
    int linesAfter;

    void init() {
        Settings settings = settingsRepository.findByIdSettings(1);
        if (settings == null) {
            System.out.println("z pliku");
            readFromFile.setPathOffset(pathoffset);
            readFromFile.setLinesAfter(linesBefore);
            readFromFile.setLinesBefore(linesAfter);
        } else {

            System.out.println("z bazy");
            readFromFile.setPathOffset(settings.getPathoffset().trim());
            readFromFile.setLinesAfter(settings.getLinesBefore());
            readFromFile.setLinesBefore(settings.getLinesAfter());

        }
        System.out.print(settings.getPathoffset());
    }

    public List<String> getNameServer() {

        List<String> nameServerList = new ArrayList<String>();
        paths = readFromFile.checkFiles();

        String onePath = null;
        String word = "gcm_";

        int positionStart = 0;
        int positionEnd = 0;

        for (int t = 0; t < paths.size(); t++) {
            onePath = paths.get(t);

            if (onePath.contains(word)) {
                char[] sentenceWords = onePath.toCharArray();
                for (int i = 0; i < sentenceWords.length; i++) {
                    if (sentenceWords[i] == word.charAt(0) && sentenceWords[i + 1] == word.charAt(1)
                            && sentenceWords[i + 2] == word.charAt(2) && sentenceWords[i + 3] == word.charAt(3)) {
                        positionStart = i;
                        for (int k = i; k < sentenceWords.length; k++) {
                            if (sentenceWords[k] == '\\') {
                                positionEnd = k;

                                nameServerList.add(paths.get(t).substring(positionStart, positionEnd));
                                break;

                            }

                        }
                    }
                }
            }
        }

        for (int k = 0; k < nameServerList.size(); k++) {
            if (k == nameServerList.size() - 1)
                break;
            if (nameServerList.get(k).equals(nameServerList.get(k + 1))) {
                nameServerList.remove(k);
                k--;
            }
        }
        Collections.sort(nameServerList, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return extractInt(o1) - extractInt(o2);
            }

            int extractInt(String s) {
                String num = s.replaceAll("\\D", "");
                return num.isEmpty() ? 0 : Integer.parseInt(num);
            }
        });
        return nameServerList;
    }

    @GetMapping("/main")
    public ModelAndView home(@RequestParam(value = "phrase", required = true) String phrase,
                             @RequestParam(value = "dateStart", required = false) String dateStart,
                             @RequestParam(value = "dateEnd", required = false) String dateEnd,
                             @RequestParam(value = "Split", required = true) Boolean split) throws IOException, ParseException {
        ModelAndView modelAndView = new ModelAndView();
        Validator validator = new Validator();
        init();

        if (dateStart.equals("")) {
            dateStartGlobal = "";
            dateStartGlobal = "";
        } else
            dateStartGlobal = dateStart;

        if (dateEnd.equals("")) {
            dateEndGlobal = "";
            dateEnd = "";

        } else
            dateEndGlobal = dateEnd;

        splitGlobal = split;
        List<String> lista = new ArrayList<String>();

        getNameServer();

        String message = new String();

        readFromFile.setPhrase(phrase);

        if (validator.validateDate(dateStart, dateEnd) != null)
            return validator.validateDate(dateStart, dateEnd);

        if (validator.validateFutureDate(dateStart, dateEnd) != null)
            return validator.validateFutureDate(dateStart, dateEnd);

        if (dateStart.equals("") && dateEnd.equals("")) {
            for (int i = 0; i < paths.size(); i++) {
                readFromFile.set(paths.get(i));
                lista.add(paths.get(i) + "(" + String.valueOf(readFromFile.amountPhrase(split)) + ")");
            }
        } else if (!dateStart.equals("") && !dateEnd.equals("") && Data.parseDate(dateStart) != null
                && Data.parseDate(dateEnd) != null) {
            for (int i = 0; i < paths.size(); i++) {
                readFromFile.set(paths.get(i));
                lista.add(paths.get(i) + "(" + String.valueOf(readFromFile.amountPhraseWithDate(dateStart, dateEnd, split))
                        + ")");
            }
        } else if (!dateStart.equals("") && dateEnd.equals("") && Data.parseDate(dateStart) != null) {
            for (int i = 0; i < paths.size(); i++) {
                readFromFile.set(paths.get(i));
                lista.add(
                        paths.get(i) + "(" + String.valueOf(readFromFile.amountPhraseWithOneDate(dateStart, split)) + ")");
            }
        } else if (dateStart.equals("") && !dateEnd.equals("") && Data.parseDate(dateEnd) != null) {
            for (int i = 0; i < paths.size(); i++) {
                readFromFile.set(paths.get(i));
                lista.add(
                        paths.get(i) + "(" + String.valueOf(readFromFile.amountPhraseWithEndDate(dateEnd, split)) + ")");
            }
        } else {
            modelAndView.setViewName("/error");
            modelAndView.addObject("message", message);
            return modelAndView;

        }

        modelAndView.addObject("nameServerList", getNameServer());
        modelAndView.addObject("list", lista);
        modelAndView.addObject("phrase", readFromFile.getPhrase());
        modelAndView.addObject("dateStart", dateStart);
        modelAndView.addObject("dateEnd", dateEnd);
        modelAndView.setViewName("/main");
        return modelAndView;
    }

    @GetMapping(value = "/show/{id}")
    public ModelAndView getShow(@PathVariable("id") int id, Model model) throws IOException, ParseException {

        ModelAndView modelAndView = new ModelAndView();
        paths = readFromFile.checkFiles();
        if (!readFromFile.getCurrentPath().equals(paths.get(id))) {
            readFromFile.set(paths.get(id));
            readFromFile.clearListBefore();
        }
        modelAndView.setViewName("view");

        try {
            List<String> logs = new ArrayList<String>();

            if (dateStartGlobal.equals("") && dateEndGlobal.equals("")) {
                logs = readFromFile.nextWithPhrase(splitGlobal);
            } else if (!dateStartGlobal.equals("") && dateEndGlobal.equals("")) {

                logs = readFromFile.nextWithDate(dateStartGlobal, splitGlobal);

            } else if (!dateStartGlobal.equals("") && !dateEndGlobal.equals("")) {

                logs = readFromFile.nextWithDate(dateStartGlobal, dateEndGlobal, splitGlobal);
            } else if (dateStartGlobal.equals("") && !dateEndGlobal.equals("")) {
                logs = readFromFile.nextWithEndDate(dateEndGlobal, splitGlobal);
            }

            modelAndView.addObject("logs", logs);
            modelAndView.addObject("middle", readFromFile.getMiddle());

        } catch (NullPointerException e) {
        }
        return modelAndView;

    }

    @GetMapping(value = "/show/{id}", params = "action=reset")
    public ModelAndView resetBuffor(@PathVariable("id") int id, Model model) throws IOException, ParseException {

        ModelAndView modelAndView = new ModelAndView();
        readFromFile.resetLines();
        List<String> logs = new ArrayList<String>();

        if (dateStartGlobal.equals("") && dateEndGlobal.equals("")) {
            logs = readFromFile.nextWithPhrase(splitGlobal);
        } else if (!dateStartGlobal.equals("") && dateEndGlobal.equals("")) {

            logs = readFromFile.nextWithDate(dateStartGlobal, splitGlobal);

        } else if (!dateStartGlobal.equals("") && !dateEndGlobal.equals("")) {

            logs = readFromFile.nextWithDate(dateStartGlobal, dateEndGlobal, splitGlobal);
        } else if (dateStartGlobal.equals("") && !dateEndGlobal.equals("")) {
            logs = readFromFile.nextWithEndDate(dateEndGlobal, splitGlobal);
        }
        modelAndView.addObject("logs", logs);
        modelAndView.addObject("middle", readFromFile.getMiddle());

        modelAndView.setViewName("view");
        return modelAndView;

    }

    @GetMapping(value = "/show/{id}", params = "action=next")
    public ModelAndView nextLine(@PathVariable("id") int id, Model model) throws IOException, ParseException {

        ModelAndView modelAndView = new ModelAndView();
        List<String> logs = new ArrayList<String>();

        logs = readFromFile.nextOneLine();

        modelAndView.addObject("logs", logs);
        modelAndView.addObject("middle", readFromFile.getMiddle());

        modelAndView.setViewName("view");
        return modelAndView;

    }

    @RequestMapping("/nextLines")
    @ResponseBody
    public String getNextLines() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        List<String> lines = new ArrayList<String>();
        lines = readFromFile.nextSeveralLines();

        for (String linia : lines) {
            stringBuilder.append(linia).append("<br />");
        }
        return stringBuilder.toString();
    }

}