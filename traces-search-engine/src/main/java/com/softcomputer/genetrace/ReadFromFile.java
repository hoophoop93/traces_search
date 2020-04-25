package com.softcomputer.genetrace;

import org.apache.commons.io.input.ReversedLinesFileReader;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ReadFromFile {

    private FileReader fileReader = null;
    private BufferedReader bufferReader = null;
    private int prevSize = 1000;
    private int counter = 0;
    private String[] arrayPreviousLines = new String[prevSize];
    private String currentPath;
    private int counterSeveralLines = 5;
    private String pathOffset;
    private int linesBefore = 5;
    private int linesAfter = 5;
    private String phrase;
    private List<String> listBefore;
    private String startDate, endDate;
    private int middle;
    private Data dateBefore;

    /**
     * This method returns searched phrase as one line or splitted into seperate words.
     * @param split decides if phrase should be splitted or not.
     * @return array of phrases to search. For not splitted phrase this table always has one element.
     */
    private String[] getSearchPhrase(boolean split) {
        return split ? getPhrase().split(" ") : new String[]{getPhrase()};
    }

    /**
     * This method finds files in given directory, which starts with given string.
     * @param dir path where files are.
     * @param dir String that all returned file names start with.
     * @return  array of found files that meet requirements.
     */
    private File[] listFiles(File dir, final String name2) {
        File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith(name2);
            }
        });
        return files != null ? files : new File[0];
    }

    /**
     * This method finds all log files in all server folders. Before using there must be.
     *  @pathOffset field initialized. Otherwise method returns empty list.
     * @return list of all logs path.
     */
    public List<String> checkFiles() {
        List<String> instances = new ArrayList<String>();
        if (StringUtils.isBlank(getPathOffset())) return instances;
        File[] directories = listFiles(new File(getPathOffset()), "gcm_");
        for (File dir : directories) {
            File[] files = listFiles(new File(dir.toString() + "\\logs"), "GcmWebServices");
            for (File file : files)
                instances.add(file.toString());
        }
        return instances;
    }

    /**
     * This method sets path to a log file. Other methods operate on that one file at the time until changed.
     * @param path log file path.
     * @throws FileNotFoundException Exception when given file does not exist.
     */
    public void set(String path) throws FileNotFoundException {
        counterSeveralLines = linesAfter;
        arrayPreviousLines = new String[prevSize];
        currentPath = path;
        fileReader = new FileReader(currentPath);
        bufferReader = new BufferedReader(fileReader);
        resetDateBefore();
    }

    /**
     * This method adds lines to array. If the table is filled, the values are changed from the beginning.
     * The method is used to hold the previous lines.
     * @param sCurrentLine is current line with file.
     * @return list with previous lines.
     * @throws IOException
     */
    public List<String> addToArray(String sCurrentLine) throws IOException {
        List<String> lista = new ArrayList<String>();
        String nextLines;
        for (int l = counter; l < linesBefore; l++)
            if (arrayPreviousLines[l] != null)
                lista.add(arrayPreviousLines[l]);

        for (int l = 0; l < counter; l++)
            if (arrayPreviousLines[l] != null)
                lista.add(arrayPreviousLines[l]);
        lista.add(sCurrentLine);
        middle = lista.size();
        bufferReader.mark(1000000);
        for (int m = 0; m < linesAfter; m++) {
            if ((nextLines = bufferReader.readLine()) != null) {
                lista.add(nextLines);
            } else
                break;
        }
        bufferReader.reset();
        arrayPreviousLines[counter] = sCurrentLine;
        counter++;
        if (counter >= linesBefore) {
            counter = 0;
        }
        return lista;
    }

    /**
     * This method adds next line to list.
     * @return a list containing new and other lines.
     * @throws IOException
     */
    public List<String> nextOneLine() throws IOException {
        counterSeveralLines = linesAfter;
        if (StringUtils.isBlank(currentPath)) return null;
        String sCurrentLine;
        if ((sCurrentLine = bufferReader.readLine()) != null) {
            listBefore = addToArray(sCurrentLine);
        }
        return listBefore;
    }

    /**
     * This method list of subsequent lines.
     * @return list of subsequent lines. Number of lines is the same as @linesAfter variable
     * @throws IOException
     */
    public List<String> nextSeveralLines() throws IOException {
        if (StringUtils.isBlank(currentPath)) return null;
        List<String> lista = new ArrayList<String>();
        String sCurrentLine;
        int temp = 1;

        bufferReader.mark(10000000);
        while ((sCurrentLine = bufferReader.readLine()) != null) {
            temp++;
            if (temp > counterSeveralLines) {
                for (int i = 0; i < linesAfter; i++) {
                    if ((sCurrentLine = bufferReader.readLine()) != null) {
                        counterSeveralLines++;
                        lista.add(sCurrentLine);
                    }
                }
                bufferReader.reset();
                return lista;
            }
        }
        return lista;
    }

    /**
     * This method close bufferReader and fileReader if existing and set fileReader,bufferReader to new variable.
     * Resets position of reader to first line
     * Additional set others variables.
     * @throws IOException
     */
    public void resetLines() throws IOException {
        counterSeveralLines = linesAfter;
        if (currentPath != null) {
            bufferReader.close();
            fileReader.close();
            fileReader = new FileReader(currentPath);
            bufferReader = new BufferedReader(fileReader);
            arrayPreviousLines = new String[prevSize];
            resetDateBefore();
        }
    }

    /**
     * This method amount occurrences of a given phrase.
     * @param split transmits information about the willingness to split the string.
     * @return counter specifying the number of phrases.
     * @throws IOException
     */
    public int amountPhrase(boolean split) throws IOException {
        if (StringUtils.isBlank(currentPath)) return 0;
        String sCurrentLine;
        int counter = 0;
        String[] temp_phrase = getSearchPhrase(split);
        while ((sCurrentLine = bufferReader.readLine()) != null) {
            for (String s : temp_phrase) {
                if (StringUtils.containsIgnoreCase(sCurrentLine, s)) {
                    counter++;
                    break;
                }
            }
        }
        return counter;
    }

    /**
     * This method amount occurrences of a given phrase between start date and end date.
     * @param startDate is starting date.
     * @param endDate is final date.
     * @param split transmits information about the willingness to split the string.
     * @return counter specifying the number of phrases.
     * @throws IOException
     * @throws ParseException
     */
    public int amountPhraseWithDate(String startDate, String endDate, boolean split) throws IOException, ParseException {
        if (StringUtils.isBlank(currentPath)) return 0;
        String sCurrentLine;
        int counter = 0;
        Data dataStart = Data.parseDate(startDate);
        Data dataEnd = Data.parseDate(endDate);
        ReversedLinesFileReader reversed = new ReversedLinesFileReader(new File(getCurrentPath()), Charset.defaultCharset());
        String reversedLine;
        while((reversedLine = reversed.readLine()) != null )
        {
            if(reversedLine.length() >18) {
                String Data = reversedLine.substring(0, 19);
                Data temp = com.softcomputer.genetrace.Data.parseDate(Data);
                if (temp == null) continue;
                else if (temp.isAfter(dataStart)) {
                    return 0;
                }
                break;
            }
        }
        String[] temp_phrase = getSearchPhrase(split);
        Data date = new Data();
        bufferReader.mark(100000);
        while ((sCurrentLine = bufferReader.readLine()) != null) {
            if (sCurrentLine.length() > 18) {
                String Data = sCurrentLine.substring(0, 19);
                Data temp = com.softcomputer.genetrace.Data.parseDate(Data);
                if (temp != null) {
                    if(dataEnd.isAfter(temp)) return 0;
                    else break;
                }
            }
        }
        bufferReader.reset();
        while ((sCurrentLine = bufferReader.readLine()) != null) {
            if (sCurrentLine.length() > 18) {
                String Data = sCurrentLine.substring(0, 19);
                Data temp = com.softcomputer.genetrace.Data.parseDate(Data);
                if (temp != null) {
                    date = temp;
                }
            }
            if ((dataStart.isAfter(date) && dataEnd.isBefore(date))
                    || date.isEqual(dataEnd) || date.isEqual(dataStart)) {
                for (String s : temp_phrase) {
                    if ((StringUtils.containsIgnoreCase(sCurrentLine, s))) {
                        counter++;
                        break;
                    }
                }
            }
            if (dataEnd.isAfter(date)) return counter;
        }
        set(currentPath);
        return counter;
    }

    /**
     * This method amount occurrences of a given phrase after start date.
     * @param startDate is starting date.
     * @param split transmits information about the willingness to split the string.
     * @return counter specifying the number of phrases.
     * @throws IOException
     * @throws ParseException
     */
    public int amountPhraseWithOneDate(String startDate, boolean split) throws IOException, ParseException {
        if (StringUtils.isBlank(currentPath)) return 0;
        String sCurrentLine;
        int counter = 0;
        Data dataStart = Data.parseDate(startDate);
        String[] temp_phrase = getSearchPhrase(split);
        ReversedLinesFileReader reversed = new ReversedLinesFileReader(new File(getCurrentPath()), Charset.defaultCharset());
        String reversedLine;
        while((reversedLine = reversed.readLine()) != null )
        {
            if(reversedLine.length() >18) {
                String Data = reversedLine.substring(0, 19);
                Data temp = com.softcomputer.genetrace.Data.parseDate(Data);
                if (temp == null) continue;
                else if (temp.isAfter(dataStart)) {
                    return 0;
                }
                break;
            }
        }
        boolean isAfter = false;
        Data date = new Data();
        while ((sCurrentLine = bufferReader.readLine()) != null) {
            if (isAfter) {
                for (String s : temp_phrase) {
                    if ((StringUtils.containsIgnoreCase(sCurrentLine, s))) {
                        counter++;
                        break;
                    }
                }
            } else if (sCurrentLine.length() > 18) {
                String Data = sCurrentLine.substring(0, 19);
                Data temp = com.softcomputer.genetrace.Data.parseDate(Data);
                if (temp != null) {
                    date = temp;
                }
            }
            if (!isAfter && ((dataStart.isAfter(date)) || date.isEqual(dataStart))) {
                for (String s : temp_phrase) {
                    if ((StringUtils.containsIgnoreCase(sCurrentLine, s))) {
                        counter++;
                        break;
                    }
                }
                isAfter = true;
            }
        }
        set(currentPath);
        return counter;
    }

    /**
     * This method find next occurrence and add it to the list, with previous lines and next lines.
     * @param split transmits information about the willingness to split the string.
     * @return list with prepared lines.
     * @throws IOException
     */
    public List<String> nextWithPhrase(boolean split) throws IOException {
        counterSeveralLines = linesAfter;
        if (StringUtils.isBlank(currentPath)) return null;
        String[] temp_phrase = getSearchPhrase(split);
        String sCurrentLine;
        while ((sCurrentLine = bufferReader.readLine()) != null) {
            for (String s : temp_phrase) {
                if ((StringUtils.containsIgnoreCase(sCurrentLine, s))) {

                    listBefore = addToArray(sCurrentLine);
                    return listBefore;
                }
            }
            arrayPreviousLines[counter] = sCurrentLine;
            counter++;
            if (counter >= linesBefore) {
                counter = 0;
            }
        }
        return listBefore;
    }
    /**
     * This method find next occurrence after starting date and add it to the list, with previous lines and next lines.
     * @param startDate is starting date.
     * @param split transmits information about the willingness to split the string.
     * @return list with prepared lines.
     * @throws IOException
     * @throws ParseException
     */
    public List<String> nextWithDate(String startDate, boolean split) throws IOException, ParseException {
        counterSeveralLines = linesAfter;
        if (StringUtils.isBlank(currentPath)) return null;
        Data dataStart = Data.parseDate(startDate);
        String[] temp_phrase = getSearchPhrase(split);
        String sCurrentLine;
        while ((sCurrentLine = bufferReader.readLine()) != null) {
            if (sCurrentLine.length() > 18) {
                String Data = sCurrentLine.substring(0, 19);
                Data temp = com.softcomputer.genetrace.Data.parseDate(Data);
                if (temp != null) {
                    setDateBefore(temp);
                }
            }

            if ( (getDateBefore().isEqual(dataStart)) || (dataStart.isAfter(getDateBefore())) ) {
                for (String s : temp_phrase) {
                    if ((StringUtils.containsIgnoreCase(sCurrentLine, s))) {
                        listBefore = addToArray(sCurrentLine);
                        return listBefore;
                    }
                }
            }
            arrayPreviousLines[counter] = sCurrentLine;
            counter++;
            if (counter >= linesBefore) {
                counter = 0;
            }
        }
        return listBefore;
    }

    /**
     * This method find next occurrence to final date and add it to the list, with previous lines and next lines.
     * @param endDate is final date.
     * @param split transmits information about the willingness to split the string.
     * @return list with prepared lines.
     * @throws IOException
     * @throws ParseException
     */
    public List<String> nextWithEndDate(String endDate, boolean split) throws IOException, ParseException {
        counterSeveralLines = linesAfter;
        if (StringUtils.isBlank(currentPath)) return null;
        Data dataEnd = Data.parseDate(endDate);
        String[] temp_phrase = getSearchPhrase(split);
        String sCurrentLine;
        while ((sCurrentLine = bufferReader.readLine()) != null) {
            if (sCurrentLine.length() > 18) {
                String Data = sCurrentLine.substring(0, 19);
                Data temp = com.softcomputer.genetrace.Data.parseDate(Data);
                if (temp != null) {
                    setDateBefore(temp);
                }
            }
            if ((dataEnd.isBefore(getDateBefore()))
                    || (getDateBefore().isEqual(dataEnd))) {
                for (String s : temp_phrase) {
                    if ((StringUtils.containsIgnoreCase(sCurrentLine, s))) {
                        listBefore = addToArray(sCurrentLine);
                        return listBefore;
                    }
                }
            }
            arrayPreviousLines[counter] = sCurrentLine;
            counter++;
            if (counter >= linesBefore) {
                counter = 0;
            }
        }
        return listBefore;
    }

    /**
     * This method amount occurrences of a given phrase to final date.
     * @param endDate is final date.
     * @param split transmits information about the willingness to split the string.
     * @return counter specifying the number of phrases.
     * @throws IOException
     * @throws ParseException
     */
    public int amountPhraseWithEndDate(String endDate, boolean split) throws IOException, ParseException {
        if (StringUtils.isBlank(currentPath)) return 0;
        String sCurrentLine;
        int counter = 0;
        Data dataEnd = Data.parseDate(endDate);
        String[] temp_phrase = getSearchPhrase(split);

        Data date = new Data();

        bufferReader.mark(100000);
        while ((sCurrentLine = bufferReader.readLine()) != null) {
            if (sCurrentLine.length() > 18) {
                String Data = sCurrentLine.substring(0, 19);
                Data temp = com.softcomputer.genetrace.Data.parseDate(Data);
                if (temp != null) {
                    if(dataEnd.isAfter(temp)) return 0;
                    else break;
                }
            }
        }
        bufferReader.reset();
        while ((sCurrentLine = bufferReader.readLine()) != null) {
            if (sCurrentLine.length() > 18) {
                String Data = sCurrentLine.substring(0, 19);
                Data temp = com.softcomputer.genetrace.Data.parseDate(Data);
                if (temp != null) {
                    date = temp;
                }
            }
            if (dataEnd.isBefore(date) || date.isEqual(dataEnd)) {
                for (String s : temp_phrase) {
                    if ((StringUtils.containsIgnoreCase(sCurrentLine, s))) {
                        counter++;
                        break;
                    }
                }
            }
            if (dataEnd.isAfter(date)) return counter;
        }
        set(currentPath);
        return counter;
    }

    /**
     * This method find next occurrence between start date and final date, add it to the list with previous lines and next lines.
     * @param startDate is starting date.
     * @param finalDate is final date.
     * @param split transmits information about the willingness to split the string.
     * @return list with prepared lines.
     * @throws IOException
     * @throws ParseException
     */
    public List<String> nextWithDate(String startDate, String finalDate, boolean split) throws IOException, ParseException {
        counterSeveralLines = linesAfter;
        if (StringUtils.isBlank(currentPath)) return null;
        Data dataStart = Data.parseDate(startDate);
        Data dataEnd = Data.parseDate(finalDate);
        String[] temp_phrase = getSearchPhrase(split);
        String sCurrentLine;
        while ((sCurrentLine = bufferReader.readLine()) != null) {
            if (sCurrentLine.length() > 18) {
                String Data = sCurrentLine.substring(0, 19);
                Data temp = com.softcomputer.genetrace.Data.parseDate(Data);
                if (temp != null) {
                    setDateBefore(temp);
                }
            }
            if ( ((dataStart.isAfter(getDateBefore()) && dataEnd.isBefore(getDateBefore())))
                    || getDateBefore().isEqual(dataEnd) || getDateBefore().isEqual(dataStart)) {
                for (String s : temp_phrase) {
                    if ((StringUtils.containsIgnoreCase(sCurrentLine, s))) {

                        listBefore = addToArray(sCurrentLine);
                        return listBefore;
                    }
                }
            }
            arrayPreviousLines[counter] = sCurrentLine;
            counter++;
            if (counter >= linesBefore) {
                counter = 0;
            }
        }
        return listBefore;
    }

    public void clearListBefore() {
        listBefore = null;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public int getLinesBefore() {
        return linesBefore;
    }

    public void setLinesBefore(int linesBefore) {
        this.linesBefore = linesBefore;
    }

    public int getLinesAfter() {
        return linesAfter;
    }

    public void setLinesAfter(int linesAfter) {
        this.linesAfter = linesAfter;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getMiddle() {
        return middle;
    }

    public void setMiddle(int middle) {
        this.middle = middle;
    }

    public String getCurrentPath() {
        return currentPath;
    }

    public List<String> getListBefore() {
        return listBefore;
    }

    public String getPathOffset() {
        return pathOffset;
    }

    public void setPathOffset(String pathOffset) {
        this.pathOffset = pathOffset;
    }

    public int getCounterSeveralLines() {
        return counterSeveralLines;
    }

    public void setCounterSeveralLines(int counterSeveralLines) {
        this.counterSeveralLines = counterSeveralLines;
    }


    public Data getDateBefore() {
        return dateBefore;
    }

    public void setDateBefore(Data dateBefore) {
        this.dateBefore = dateBefore;
    }
    public void resetDateBefore()
    {
        dateBefore  = new Data();
    }
}