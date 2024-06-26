package com.example.ayusidehiddengemsplaylistback.service;

import com.example.ayusidehiddengemsplaylistback.form.MemberForm;
import com.example.ayusidehiddengemsplaylistback.form.PlaylistForm;
import com.example.ayusidehiddengemsplaylistback.repository.MemberRepository;
import com.example.ayusidehiddengemsplaylistback.repository.PlaylistRepository;
import com.example.ayusidehiddengemsplaylistback.entity.Member;
import com.example.ayusidehiddengemsplaylistback.entity.Playlist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private final MemberRepository memberRepository;
    private final PlaylistRepository playlistRepository;

    @Autowired
    public SearchService(MemberRepository memberRepository, PlaylistRepository playlistRepository) {
        this.memberRepository = memberRepository;
        this.playlistRepository = playlistRepository;
    }

    public Map<String, Object> search(String keyword) {
        List<Member> members = memberRepository.findByNameContaining(keyword);
        List<Playlist> playlists = playlistRepository.findByPlaylistTitleContaining(keyword);

        List<MemberForm> memberForms = members.stream().map(member -> new MemberForm(member.getName())).collect(Collectors.toList());
        List<PlaylistForm> playlistForms = playlists.stream().map(playlist -> new PlaylistForm(playlist.getPlaylistTitle())).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        if (memberForms.isEmpty() && playlistForms.isEmpty()) {
            result.put("message", "검색 결과가 없습니다.");
        } else {
            result.put("members", memberForms);
            result.put("playlists", playlistForms);
        }

        return result;
    }
}
