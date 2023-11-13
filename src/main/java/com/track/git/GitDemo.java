package com.track.git;

import com.track.until.JGitUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GitDemo {

    public static void main(String[] args) throws GitAPIException {

        String localPath = "C:\\Users\\track\\Desktop\\我的\\考研\\postgraduate";

        // 分支名
        String branch = "english";

        // 生成身份信息  远程操作的时候使用
        CredentialsProvider provider = new UsernamePasswordCredentialsProvider("gan141425@163.com", "141425gzk");
        System.out.println(provider);

        Git git = JGitUtils.openRpo(localPath); //获取git对象
        System.out.println(git);

        // 获取所有分支
        List<org.eclipse.jgit.lib.Ref> call = git.branchList().call();     //得到所有分支信息
        for(Ref ref : call) System.out.println(ref.getName());

        // add
        git.add().addFilepattern("英语句子.md").call();

        // commit
//        git.commit().setMessage("java commit").call();

        // pull
        git.pull()
                .setRemoteBranchName(branch)  //设置需要pull的远端分支
                .setCredentialsProvider(provider)  //身份验证
                .call();

        // push
        git.push()
                .setRemote("origin")    //设置推送的URL名称
                .setRefSpecs(new RefSpec(branch))   //设置需要推送的分支,如果远端没有则创建
                .setCredentialsProvider(provider)   //身份验证
                .call();


        Map<String,String> map = new HashMap<String,String>();
        Status status = git.status().call();
        map.put("Added",status.getAdded().toString());
        map.put("Changed",status.getChanged().toString());
        map.put("Conflicting",status.getConflicting().toString());
        map.put("ConflictingStageState",status.getConflictingStageState().toString());
        map.put("IgnoredNotInIndex",status.getIgnoredNotInIndex().toString());
        map.put("Missing",status.getMissing().toString());
        map.put("Modified",status.getModified().toString());
        map.put("Removed",status.getRemoved().toString());
        map.put("UntrackedFiles",status.getUntracked().toString());
        map.put("UntrackedFolders",status.getUntrackedFolders().toString());
        System.out.println(map);

        // 建立分支
//        git.branchCreate()
//                .setName("dev") //创建的分支名字
//                .call();

        // 删除分支
//        git.branchDelete()
//                .setBranchNames("dev")   //设置删除分支的名字
//                .call();

        // 切换分支
//        git.checkout()
//                .setName("dev") //设置分支名
//                .call();





    }

}
