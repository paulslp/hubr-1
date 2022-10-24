package ru.gb.hubr.service.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gb.hubr.api.user.UserDto;
import ru.gb.hubr.api.user.profile.ProfileService;
import ru.gb.hubr.dao.AccountUserDao;
import ru.gb.hubr.entity.AccountUser;
import ru.gb.hubr.service.mapper.UserMapper;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileServiceSQL implements ProfileService {

    private final AccountUserDao accountUserDao;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public UserDto findByLogin(String login) {

        AccountUser accountUser = accountUserDao.findByLogin(login).orElse(new AccountUser());
        UserDto userDto = userMapper.toUserDto(accountUser);
        return userDto;

    }

    @Override
    public UserDto findById(Long idUser) {
        AccountUser accountUser = accountUserDao.findById(idUser).orElseThrow();
        UserDto userDto = userMapper.toUserDto(accountUser);

        return userDto;
    }

    @Override
    @Transactional
    public UserDto save(UserDto userDto) {

        AccountUser accountUser = userMapper.toAccountUser(userDto);

        if (accountUser.getLogin() != null) {
            accountUserDao.findByLogin(accountUser.getLogin())
                    .ifPresent((p) -> accountUser.setVersion(p.getVersion()));
        }
        return userMapper.toUserDto(accountUserDao.save(accountUser));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return accountUserDao.findAll().stream()
                .map(userMapper::toUserDto).collect(Collectors.toList());
    }




    @Override
    @Transactional(readOnly = true)
    public UserDto getCurrentUser(HttpSession session) {

        AccountUser account_user = (AccountUser) session.getAttribute("account_user");
        return userMapper.toUserDto(account_user);

    }

}
