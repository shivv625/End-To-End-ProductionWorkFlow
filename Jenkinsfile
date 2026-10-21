pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
            }
        }

        stage('Build') {
            steps {
                echo 'Building Spring Boot application...'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Building Docker image...'
                sh 'docker build -t shiv:1.0 .'
            }
        }

        stage('Docker Push') {
            steps {
                echo 'Pushing Docker image to Docker Hub...'

                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub',
                        usernameVariable: 'DOCKER_USERNAME',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )
                ]) {
                    sh '''
                        echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
                        docker tag shiv:1.0 shivv625/shiv:1.0
                        docker push shivv625/shiv:1.0
                        docker logout
                    '''
                }
            }
        }

        stage('Deploy to EC2') {
            steps {
                echo 'Deploying application to EC2 using Ansible...'

                withCredentials([
                    sshUserPrivateKey(
                        credentialsId: 'ec2-ssh',
                        keyFileVariable: 'SSH_KEY',
                        usernameVariable: 'SSH_USER'
                    )
                ]) {
                    sh '''
                        export ANSIBLE_HOST_KEY_CHECKING=False

                        cat > inventory.ini <<EOF
[app]
ec2 ansible_host=65.1.132.171 ansible_user=$SSH_USER ansible_ssh_private_key_file=$SSH_KEY
EOF

                        ansible-playbook -i inventory.ini setup.yml
                    '''
                }
            }
        }
    }
}