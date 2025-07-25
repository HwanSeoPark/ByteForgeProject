package com.byteforge.account.user.domain;

import com.byteforge.account.profile.domain.Profile;
import com.byteforge.account.user.constant.UserRole;
import com.byteforge.account.user.constant.UserType;
import com.byteforge.account.user.dto.LoginRequest;
import com.byteforge.common.config.BooleanConverter;
import com.byteforge.post.post.domain.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.IntSequenceGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = IntSequenceGenerator.class , property = "id")
public class User implements UserDetails {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userKey;
	
	@Column(nullable = false , length = 30, unique = true)
	private String id;

	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false)
	private String password;

	private String profileImage;

	private LocalDate suspensionDate;

	@Column(nullable = false)
	@Convert(converter = BooleanConverter.class)
	private boolean isSuspension;

	@Column(nullable = false)
	@Convert(converter = BooleanConverter.class)
	private boolean isDelete;

	private String suspensionReason;

	@Column(nullable = false)
	private LocalDate lastLoginDate;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false)
	@Setter
	private UserRole role;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false)
	private UserType userType;

	@Temporal(TemporalType.DATE)
	@CreationTimestamp
	@JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy/MM/dd" , timezone = "Asia/Seoul")
	@Column(nullable = false)
	private LocalDate joinDate;

	@Builder.Default
	@OneToOne(fetch = FetchType.EAGER , cascade = CascadeType.ALL)
	@JoinColumn(nullable = false)
	private Profile profile = new Profile();

	@Builder.Default
	@OneToMany(mappedBy = "writer" , cascade = CascadeType.ALL)
	private List<Post> post = new ArrayList<>();

	public void addSuspensionDate(int date, String reason) {
		if(!isSuspension || suspensionDate == null) {
			isSuspension = true;

			suspensionDate = LocalDate.now().plusDays(date);
			suspensionReason = reason;
		}

		suspensionDate = suspensionDate.plusDays(date);
	}

	public void updatePassword(String password) {
		this.password = password;
	}

	public void updateLoginDate() {
		lastLoginDate = LocalDate.now();
	}

	public void minusSuspensionDate(int date) {
		if(LocalDate.now().compareTo(suspensionDate.minusDays(date)) > 0) {
			isSuspension = false;

			suspensionDate = LocalDate.now();
		}

		suspensionDate = suspensionDate.minusDays(date);
	}

	public void deleteUser() {
		this.isDelete = true;
	}

	public static User createGeneralUser(LoginRequest loginRequest , String url , String password) {
		return User.builder()
				.id(loginRequest.getId())
				.profileImage(url)
				.password(password)
				.email(loginRequest.getEmail())
				.profile(Profile.createInitProfileSetting())
				.lastLoginDate(LocalDate.now())
				.isSuspension(false)
				.role(UserRole.USER)
				.userType(UserType.GENERAL_USER)
				.isDelete(false)
				.build();
	}

	public static User createOAuthUser(String userId, String email) {
		return User.builder()
				.id(userId + generateRandomString(6))
				.email(email)
				.password(UUID.randomUUID().toString())
				.profile(Profile.createInitProfileSetting())
				.lastLoginDate(LocalDate.now())
				.isSuspension(false)
				.role(UserRole.USER)
				.userType(UserType.OAUTH_USER)
				.isDelete(false)
				.build();
	}

	public static String generateRandomString(int length) {
		// 랜덤 문자열을 포함할 문자들
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder sb = new StringBuilder();
		Random random = new Random();

		for (int i = 0; i < length; i++) {
			char randomChar = characters.charAt(random.nextInt(characters.length()));
			sb.append(randomChar);
		}

		return sb.toString();
	}

	public void addPost(Post post) {
		this.post.add(post);
		post.setWriter(this);
	}

	public boolean checkPassword(String plainPassword, PasswordEncoder passwordEncoder) {
		return passwordEncoder.matches(plainPassword, this.password);
	}

	public void updateId(String userId) {
		this.id = userId;
	}

	public void updateProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority(role.getRole()));
	}

	@Override
	public String getUsername() {
		return id;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}
}
