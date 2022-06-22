package com.skhu.cloud.service.Impl;

import com.skhu.cloud.constant.Const;
import com.skhu.cloud.dto.DirectoryDto;
import com.skhu.cloud.dto.FileDto;
import com.skhu.cloud.service.MainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MainServiceImpl implements MainService {

    @Override
    public List<DirectoryDto> getDirectoryList(String path) { // 현재 Path 를 가지고 Parent 의 경로들을 List로 반환 (상단바를 클릭하였을 때, 원하는 경로로 이동할 수 있도록)
        List<DirectoryDto> result = new ArrayList<>();
        int index = 0;

        while(true){
            if(path.indexOf("/" , index) == path.lastIndexOf("/")) {
                result.add(DirectoryDto.createDirectoryDto(path));
                break; // 전체를 넘기고 바로 끝
            }

            index = path.indexOf("/" , ++index); // 이러면 다음 / 를 찾을 수 있음
            result.add(DirectoryDto.createDirectoryDto(path.substring(0 , index))); // 이러면 계속해서 다음 것을 찾을 것임
        }

        return result;
    } // list 를 return

    @Override
    public List<FileDto> createFileDtoList(String path, String sortBy, String direction) throws IOException { // 주어진 정렬 조건을 가지고 정렬을 한 상태로 하위 파일들을 반환
        File file = new File(path);
        File[] files = file.listFiles();
        List<FileDto> result = new ArrayList<>();

        if (files != null) {
            for (File f : files) {
                result.add(FileDto.createFileDto(f));
            }
        }

        sortByFileDtoList(result, sortBy, direction);
        return result;
    }

    @Override
    public List<FileDto> pagingFileDtoList(List<FileDto> fileDtoList, Long pageNumber) throws IOException { // List 를 조건에 맞게 Paging 해서 넘겨줌
        int size = fileDtoList.size(); // 사이즈를 파악하는 것이 우선시, subList 도 substring 과 굉장히 흡사
        int start = (int) (Const.PAGE_SIZE * pageNumber);

        if (start + Const.PAGE_SIZE < size) { // 현재 선택한 페이지가 PAGE SIZE 만큼의 요소가 있을 때
            fileDtoList =  fileDtoList.subList(start, (int) (start + Const.PAGE_SIZE));
        } else { // 부족할 때
            fileDtoList = fileDtoList.subList(start, size);
        }

        return fileDtoList;
    }

    @Override
    public Long[] calculatePageNumber(List<FileDto> fileDtoList, Long jump, Long pageNumber) { // Pagination 에서 필요한 정보를 반환
        Long totalSize = (long) Math.ceil((double) fileDtoList.size() / Const.PAGE_SIZE);

        if (jump != null) { // 0 이 아닐때에만 진행하도록, 점프할때 점프한 페이지의 첫 페이지로 이동할 수 있도록
            Long tempPageNumber = pageNumber;
            tempPageNumber = ((tempPageNumber - 1 + jump) / Const.PAGE_SIZE) * Const.PAGE_SIZE + 1; // 현재 그렇게 만들어놓은 상태이다.

            if (tempPageNumber < 0) {
                pageNumber = Const.INIT_PAGE_NUMBER;
            } else if (tempPageNumber <= totalSize) { // jump 한 page Number 가 음수가 된 경우는 무조건 1 페이지로 이동해야 한다.
                pageNumber = tempPageNumber;
            } else { // 아얘 tempPageNumber 마저 넘어버리면 그냥 totalSize 를 pageNumber 로 해주자.
                pageNumber = totalSize;
            }
        }

        Long start = ((pageNumber - 1) / Const.PAGE_SIZE) * Const.PAGE_SIZE + 1;
        Long end = ((pageNumber - 1) / Const.PAGE_SIZE) * Const.PAGE_SIZE + Const.PAGE_SIZE;

        if (end > totalSize) {
            end = totalSize;
        }

        return new Long[] {pageNumber, start, end};
    }

    @Override
    public List<FileDto> findSubFile(String path, String[] key) throws IOException { // 현재 디렉토리로 부터 검색어로 들어온 파일을 검색 (? 로 연결하면 다중 검색도 가능)
        Queue<Object[]> queue = new LinkedList<>(); // 하위 디렉토리 탐색을 도와줄 Queue
        List<FileDto> result = new ArrayList<>(); // 결과를 담을 List
        queue.add(new Object[] {new File(path), 1}); // Depth 를 8로 제한하기 위해서 Depth 도 Queue 에다가 같이 넣어준다.

        while (!queue.isEmpty()) {
            Object[] object = queue.poll();
            File file = (File) object[0]; // Object 에서 File 을 빼온다.
            int depth = (int) object[1]; // depth 를 명시

            if (matchKeyWord(file, key)) {
                result.add(FileDto.createFileDto(file));
            }

            if (depth == Const.DEPTH_LIMIT || file.getName().equals(Const.ACCESS_PROHIBIT_FILE)) { // depth LIMIT 에 도달하면 그냥 반환하고, Containers 에 접근하면 끝낸다 (Desktop과 연결되어 있어 불필요한 중복이 생기게됨)
                continue;
            }

            if (file.isDirectory()) { // directory 인 경우에만 진행한다.
                File[] files = file.listFiles();

                if (files != null) {
                    for (File innerFile : files) {
                        queue.add(new Object[] {innerFile, depth + 1});
                    }
                }
            }
        }

        Collections.sort(result, (f1, f2) -> -f1.getKind().compareToIgnoreCase(f2.getKind()));
        return result;
    }

    @Override
    public String getRootPath() { // 운영체제 에 따라서 Root Path 를 반환
        String osName = System.getProperty("os.name").toLowerCase(); // OS name 을 얻어낸다.
        String result = "";

        if (osName.contains("win")) { // 윈도우인 경우
            result = "c:\\";
        }

        if (osName.contains("mac")) { // mac 인 경우
            result = "/Users";
        }

        if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) { // linux 인 경우
            result = "/home";
        }

        return result;
    }

    @Override
    public List<FileDto> sortByFileDtoList(List<FileDto> fileDtoList, String sortBy, String direction) { // 실제로 정렬을 해주는 파트
        if (!(sortBy.isBlank() || direction.isBlank())) { // 하나라도 비어있으면 따로 정렬을 하지 않는다.
            Collections.sort(fileDtoList, returnComparator(sortBy, direction));
        }

        Collections.sort(fileDtoList, (f1, f2) -> -f1.getKind().compareToIgnoreCase(f2.getKind())); // reverse 로 정렬하여 넘겨준다.
        return fileDtoList;
    }

    @Override
    public String readFile(String path) throws IOException { // 해당 경로의 파일의 내용을 읽어옴
        // 계층적인 방법을 이용하여서 속도 향상 및 인코딩 변경이 가능함
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path) , "UTF-8"));

        StringBuilder sb = new StringBuilder();

        while (true) {
            String string = reader.readLine();

            if (string == null) {
                break;
            }

            sb.append(string + "\r\n"); // 잘 출력된다 , content 로서 넘어갈 때 , 문제가 있는 듯
        }

        return sb.toString();
    }

    @Override
    public void directoriesAddObject(ModelAndView mvc, List<DirectoryDto> directoryList, List<FileDto> fileDtoList, String path,
            Long nowPage, Long startNumber, Long endNumber, String sortBy, String direction) {
        mvc.addObject("directoryList" , directoryList);
        mvc.addObject("fileList" , fileDtoList);
        mvc.addObject("nowPath", path);
        mvc.addObject("nowPage", nowPage);
        mvc.addObject("startNumber", startNumber);
        mvc.addObject("endNumber", endNumber);
        mvc.addObject("sortBy", sortBy);
        mvc.addObject("direction", direction);
    }

    @Override
    public void filesMvcAddObject(ModelAndView mvc, boolean diff, String extension,
                                  String path, String title) throws IOException{
        // 원래 content 를 직접적으로 넘겼었지만 , versionList 에 담겨있는 content 를 넘기는 식으로 request header to large 문제를 해결 하였음
        mvc.addObject("extension" , extension);
        mvc.addObject("path" , path);
        mvc.addObject("content", readFile(path));
        mvc.addObject("title" , title);
        mvc.addObject("diff", diff);
    }

    @Override
    public void searchMvcAddObject(ModelAndView mvc, String nowPath, List<FileDto> fileList) {
        mvc.addObject("nowPath", nowPath);
        mvc.addObject("fileList", fileList);
    }

    @Override
    public String getComponentName(String path){ // File name 을 얻어옴
        // 만약 / 가 없다면 -1 을 반환한다.
        int lastIndex = path.lastIndexOf("/");

        if(lastIndex == -1) {
            return path;
        } else {
            return path.substring(lastIndex + 1);
        }
    }

    @Override
    public List<String> getTimeList() { // 차트에서 사용되는 Mock Time
        List<String> result = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            String[] time = FileDto.modifiedTime(
                    LocalDateTime.now()
                    .atZone(ZoneId.systemDefault())
                    .toInstant().toEpochMilli()).split(" ");

            result.add(time[3] + " " + time[4]);
        }

        return result;
    }

    @Override
    public List<Long> getCodeList() { // 차트에서 사용되는 Mock Code 양
        List<Long> result = new ArrayList<>();

        // 지금은 코드가 그럴싸 해 보이도록 조금 바꾸었음
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            result.add(new Long(random.nextInt(500)));
        }

        return result;
    }

    public boolean matchKeyWord(File file, String[] key) { // findSubFile 메소드 에서 File name 과 KeyWord 가 일치하는지 확인해주는 메소드
        for (int i = 0; i < key.length; i++) {
            if (file.getName().toLowerCase().contains(key[i].toLowerCase())) { // 일치하는 것이 있으면 true 를 반환
                return true;
            }
        }

        return false; // 일치하지 않으면 false 를 반환
    }

    public Comparator<FileDto> returnComparator(String sortBy, String direction) { // 정렬 필드, 정렬 조건이 넘어오면 그에 맞는 람다를 반환 (Comparator)
        // map 으로 Comparator 를 관리해보자.
        HashMap<String, Comparator<FileDto>> map = new HashMap<>();
        String key = sortBy + " " + direction;

        map.put("name asc", (f1, f2) -> f1.getName().compareToIgnoreCase(f2.getName()));
        map.put("name desc", (f1, f2) -> -f1.getName().compareToIgnoreCase(f2.getName()));

        map.put("path asc", (f1, f2) -> f1.getPath().compareToIgnoreCase(f2.getPath()));
        map.put("path desc", (f1, f2) -> -f1.getPath().compareToIgnoreCase(f2.getPath()));

        map.put("modifiedTime asc", (f1, f2) -> Long.compare(f1.getLongModifiedTime(), f2.getLongModifiedTime()));
        map.put("modifiedTime desc", (f1, f2) -> -Long.compare(f1.getLongModifiedTime(), f2.getLongModifiedTime()));

        map.put("size asc", (f1, f2) -> Long.compare(f1.getLongSize(), f2.getLongSize()));
        map.put("size desc", (f1, f2) -> -Long.compare(f1.getLongSize(), f2.getLongSize()));

        return map.get(key); // comparator 반환
    }
}
