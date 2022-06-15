package com.skhu.cloud.service.Impl;

import com.skhu.cloud.constant.Const;
import com.skhu.cloud.dto.DirectoryDto;
import com.skhu.cloud.dto.FileDto;
import com.skhu.cloud.dto.version.FileVersionDto;
import com.skhu.cloud.service.MainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MainServiceImpl implements MainService {

    @Override
    public List<DirectoryDto> getDirectoryList(String path) {
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
    public List<FileDto> createFileDtoList(String path, String sortBy, String direction) throws IOException { // 그냥 아얘 kind 를 가지고 정렬을 진행하자.
        File file = new File(path);
        File[] files = file.listFiles();
        List<FileDto> result = new ArrayList<>();

        if (files != null) {
            for (File f : files) {
                result.add(FileDto.createFileDto(f));
            }
        }

        sortByFileDtoList(result, sortBy, direction);
        Collections.sort(result, (f1, f2) -> -f1.getKind().compareToIgnoreCase(f2.getKind())); // reverse 로 정렬하여 넘겨준다.
        return result;
    }

    @Override
    public List<FileDto> pagingFileDtoList(List<FileDto> fileDtoList, Long pageNumber) throws IOException {
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
    public Long[] calculatePageNumber(List<FileDto> fileDtoList, Long jump, Long pageNumber) {
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

        return new Long[] {pageNumber, totalSize, start, end};
    }

    @Override
    public List<FileDto> findSubFile(String path, String key) throws IOException {
        Queue<Object[]> queue = new LinkedList<>(); // 하위 디렉토리 탐색을 도와줄 Queue
        List<FileDto> result = new ArrayList<>(); // 결과를 담을 List
        queue.add(new Object[] {new File(path), 1}); // Depth 를 8로 제한하기 위해서 Depth 도 Queue 에다가 같이 넣어준다.

        while (!queue.isEmpty()) {
            Object[] object = queue.poll();
            File file = (File) object[0]; // Object 에서 File 을 빼온다.
            int depth = (int) object[1]; // depth 를 명시

            if (file.getName().contains(key)) { // 조건에 부합하면 result 에 포함시킴
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

    public List<FileDto> sortByFileDtoList(List<FileDto> fileDtoList, String sortBy, String direction) throws IOException {
        if (!(sortBy.isBlank() || direction.isBlank())) { // 하나라도 비어있으면 따로 정렬을 하지 않는다.
            Collections.sort(fileDtoList, returnComparator(sortBy, direction));
        }

        return fileDtoList;
    }

    public Comparator<FileDto> returnComparator(String sortBy, String direction) throws IOException {
        // map 으로 Comparator 를 관리해보자.
        HashMap<String, Comparator<FileDto>> map = new HashMap<>();
        String key = sortBy + " " + direction;

        map.put("name asc", (f1, f2) -> f1.getName().compareToIgnoreCase(f2.getName()));
        map.put("name desc", (f1, f2) -> -f1.getName().compareToIgnoreCase(f2.getName()));

        map.put("modifiedTime asc", (f1, f2) -> Long.compare(f1.getLongModifiedTime(), f2.getLongModifiedTime()));
        map.put("modifiedTime desc", (f1, f2) -> -Long.compare(f1.getLongModifiedTime(), f2.getLongModifiedTime()));

        map.put("size asc", (f1, f2) -> Long.compare(f1.getLongSize(), f2.getLongSize()));
        map.put("size desc", (f1, f2) -> -Long.compare(f1.getLongSize(), f2.getLongSize()));

        return map.get(key); // comparator 반환
    }

    @Override
    public boolean isDirectory(String path) {
        return new File(path).isDirectory(); // directory 면 true , 아니면 false 를 반환한다.
    }

    @Override
    public String readFile(String path) throws IOException {
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
    public void mvcAddObject(ModelAndView mvc, List<DirectoryDto> directoryList, List<FileDto> fileDtoList) {
        mvc.addObject("directoryList" , directoryList);
        mvc.addObject("fileList" , fileDtoList);
    }

    @Override
    public void filesMvcAddObject(ModelAndView mvc , String extension, List<FileVersionDto> versionList,
                                  List<String> time, List<Long> code, String path, Long index , String title) throws IOException{
        // 원래 content 를 직접적으로 넘겼었지만 , versionList 에 담겨있는 content 를 넘기는 식으로 request header to large 문제를 해결 하였음
        mvc.addObject("extension" , extension);
        mvc.addObject("versionList" , versionList);
        mvc.addObject("time" , time);
        mvc.addObject("code" , code);
        mvc.addObject("path" , path);
        mvc.addObject("content" , versionList.get(index.intValue()).getContent());
        mvc.addObject("index" , index);
        mvc.addObject("title" , title);
    }

    @Override
    public void versionMvcAddObject(ModelAndView mvc , String extension, List<FileVersionDto> versionList,
                                    List<String> time, List<Long> code, String path, Long index , String title) throws IOException{
        // 원래 content 를 직접적으로 넘겼었지만 , versionList 에 담겨있는 content 를 넘기는 식으로 request header to large 문제를 해결 하였음
        mvc.addObject("extension" , extension);
        mvc.addObject("versionList" , versionList);
        mvc.addObject("time" , time);
        mvc.addObject("code" , code);
        mvc.addObject("path" , path);
        mvc.addObject("content1" , versionList.get(index.intValue()).getContent());
        if(versionList.size() - 1 != index) {// 제일 초기 버전일 때에는 content 2 를 넘기지 않는다.
            mvc.addObject("content2", versionList.get(index.intValue() + 1).getContent());
        }
        mvc.addObject("index" , index);
        mvc.addObject("title" , title);
    }

    @Override
    public String getComponentName(String path){
        // 만약 / 가 없다면 -1 을 반환한다.
        int lastIndex = path.lastIndexOf("/");

        if(lastIndex == -1) {
            return path;
        }

        else return path.substring(lastIndex + 1);
    }

    @Override
    public List<FileVersionDto> getVersionList(String path){
        List<FileVersionDto> result = new ArrayList<>();
        // path 추가(생성자)
        try {
            for (int i = 0; i < 10; i++) {
                if(i == 0) result.add(new FileVersionDto(LocalDateTime.now() , readFile(path)));
                else result.add(new FileVersionDto(LocalDateTime.now(), "안녕하세요" + i));
            }
        } catch(IOException e){ // throws IOException 처리
            log.error("error code : " + e.getMessage());
        }
        return result;
    }

    // 해당 versionDto List 에서 Time 만 빼내서 표현
    @Override
    public List<String> getTimeList(List<FileVersionDto> fileVersionDtoList) {
        List<String> result = new ArrayList<>();
        for(FileVersionDto fileVersionDto : fileVersionDtoList){
            result.add(fileVersionDto.getTimeToString());
        }
        return result;
    }

    // 해당 versionDto List 에서 Code양 만 빼내서 표현
    @Override
    public List<Long> getCodeList(List<FileVersionDto> fileVersionDtoList) {
        List<Long> result = new ArrayList<>();

        // 지금은 코드가 그럴싸 해 보이도록 조금 바꾸었음
        Random random = new Random();

        for(FileVersionDto fileVersionDto : fileVersionDtoList){
            result.add(new Long(fileVersionDto.getContent().length()) + random.nextInt(500));
        }
        return result;
    }
}
